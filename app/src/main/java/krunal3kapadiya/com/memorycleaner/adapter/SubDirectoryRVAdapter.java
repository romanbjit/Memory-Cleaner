package krunal3kapadiya.com.memorycleaner.adapter;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import krunal3kapadiya.com.memorycleaner.R;

import static krunal3kapadiya.com.memorycleaner.MainActivity.getFolderSize;

/**
 * Created by Krunal on 8/28/2017.
 */

public class SubDirectoryRVAdapter extends RecyclerView.Adapter<SubDirectoryRVAdapter.ViewHolder> {
    private Context mContext;
    private File[] mSubDirectory;

    public SubDirectoryRVAdapter(Context context, File[] subDirectory) {
        mContext = context;
        mSubDirectory = subDirectory;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_app_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSubTitle.setText("> " + mSubDirectory[position].getName());
//        ThirdLevelRVAdapter thirdLevelRVAdapter = new ThirdLevelRVAdapter(mContext, mSubDirectory[position].listFiles());
//        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        holder.mRecyclerView.setAdapter(thirdLevelRVAdapter);

        holder.mAppSize.setText(AppListRVAdapter.formatSize(getFolderSize((mSubDirectory[position])) / 1024));
    }

    @Override
    public int getItemCount() {
        return mSubDirectory.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mSubTitle;
        TextView mAppSize;
//        RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            mSubTitle = itemView.findViewById(R.id.row_app_title);
//            mRecyclerView = itemView.findViewById(R.id.row_app_rv);
            mAppSize = itemView.findViewById(R.id.row_app_size);
        }
    }
}