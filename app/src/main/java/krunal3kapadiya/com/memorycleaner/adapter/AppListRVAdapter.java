package krunal3kapadiya.com.memorycleaner.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import krunal3kapadiya.com.memorycleaner.Constants;
import krunal3kapadiya.com.memorycleaner.MainActivity;
import krunal3kapadiya.com.memorycleaner.R;

/**
 * Created by Krunal on 8/22/2017.
 */

public class AppListRVAdapter extends RecyclerView.Adapter<AppListRVAdapter.ViewHolder> {
    private Context mContext;
    //    private File[] mAppDataArrayList;
    private OnItemClickListener mListener;
    private ArrayList<File> mFileArrayList;

    public AppListRVAdapter(Context context, File[] appData, OnItemClickListener listener) {
        mContext = context;
//        mAppDataArrayList = appData;
        mFileArrayList = new ArrayList<>();
        Collections.addAll(mFileArrayList, appData);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_app_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSize.setText(formatSize(MainActivity.getFolderSize((mFileArrayList.get(position))) / 1024));
        holder.mTitle.setText(mFileArrayList.get(position).getName());

        int color = ContextCompat.getColor(mContext, Constants.getColorResource(mFileArrayList.get(position)));

        Drawable drawable = ContextCompat.getDrawable(mContext, Constants.getImageResource(mFileArrayList.get(position)));
//        DrawableCompat.setTint(drawable, Color.rgb(255, 255, 255));

        DrawableCompat.setTint(drawable, color);

        Drawable drawableDelete = ContextCompat.getDrawable(mContext, R.drawable.ic_delete);
        DrawableCompat.setTint(drawableDelete, ContextCompat.getColor(mContext, R.color.directory));
        holder.mDelete.setImageDrawable(drawableDelete);

        holder.mImageView.setImageDrawable(drawable);


    }

    @Override
    public int getItemCount() {
        if (mFileArrayList != null)
            return mFileArrayList.size();
        else
            return 0;
    }

    public void setData(@NonNull File[] subDirectory) {
        mFileArrayList.clear();
        Collections.addAll(mFileArrayList, subDirectory);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mLayout;
        TextView mTitle;
        TextView mSize;
        ImageView mImageView;
        ImageView mDelete;

        ViewHolder(View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.row_layout);
            mTitle = itemView.findViewById(R.id.row_app_title);
            mSize = itemView.findViewById(R.id.row_app_size);
            mImageView = itemView.findViewById(R.id.row_app_image);
            mDelete = itemView.findViewById(R.id.row_button_delete);

            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(getAdapterPosition());
                }
            });
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onDeleteClick(getAdapterPosition());
                }
            });
        }
    }

    public static String formatSize(long size) {
        String value;
        if (size >= 1024)
            value = size / 1024 + " Mb";
        else
            value = size + " Kb";
        return value;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }
}
