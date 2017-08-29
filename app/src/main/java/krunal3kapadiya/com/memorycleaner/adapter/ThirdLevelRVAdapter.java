package krunal3kapadiya.com.memorycleaner.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import krunal3kapadiya.com.memorycleaner.R;

import static krunal3kapadiya.com.memorycleaner.MainActivity.getFolderSize;

/**
 * Created by Krunal on 8/28/2017.
 */

public class ThirdLevelRVAdapter extends RecyclerView.Adapter<ThirdLevelRVAdapter.ViewHolder>
{
    private Context mContext;
    private File[] mSubDirectory;

    public ThirdLevelRVAdapter(Context context, File[] subDirectory)
    {
        mContext = context;
        mSubDirectory = subDirectory;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_app_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.mTitle.setText(">>" + mSubDirectory[position].getName());

        holder.mAppSize.setText(AppListRVAdapter.formatSize(getFolderSize((mSubDirectory[position])) / 1024));
    }

    @Override
    public int getItemCount()
    {
        return mSubDirectory.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTitle;
        TextView mAppSize;
        ImageView mDelete;

        ViewHolder(View itemView)
        {
            super(itemView);
            mTitle = itemView.findViewById(R.id.row_app_title);
            mDelete = itemView.findViewById(R.id.row_button_delete);
            mAppSize = itemView.findViewById(R.id.row_app_size);

        }
    }
}
