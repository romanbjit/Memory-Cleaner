package krunal3kapadiya.com.memorycleaner.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import krunal3kapadiya.com.memorycleaner.R;
import krunal3kapadiya.com.memorycleaner.data.AppData;

/**
 * Created by Krunal on 8/22/2017.
 */

public class AppListRVAdapter extends RecyclerView.Adapter<AppListRVAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<AppData> mAppDataArrayList;

    public AppListRVAdapter(Context context, ArrayList<AppData> appData) {
        mContext = context;
        mAppDataArrayList = appData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_app_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSize.setText(formatSize(mAppDataArrayList.get(position).getAppSize()));
        holder.mTitle.setText(mAppDataArrayList.get(position).getAppName());

        SubDirectoryRVAdapter subDirectoryRVAdapter = new SubDirectoryRVAdapter(mContext, mAppDataArrayList.get(position).getSubDirectory());
        holder.mSubList.setLayoutManager(new LinearLayoutManager(mContext));
        holder.mSubList.setAdapter(subDirectoryRVAdapter);

    }

    @Override
    public int getItemCount() {
        return mAppDataArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mSize;
        RecyclerView mSubList;

        ViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.row_app_title);
            mSize = itemView.findViewById(R.id.row_app_size);
            mSubList = itemView.findViewById(R.id.row_app_rv);
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
}
