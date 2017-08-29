package krunal3kapadiya.com.memorycleaner.data;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Krunal on 8/22/2017.
 */

public class AppData {
    private String mAppName;
    private long mAppSize;
    private File[] mSubDirectoryPath;

    public AppData(String appName, long appSize, File[] files) {
        mAppName = appName;
        mAppSize = appSize;
        mSubDirectoryPath = files;
    }

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String appName) {
        mAppName = appName;
    }

    public long getAppSize() {
        return mAppSize;
    }

    public void setAppSize(long appSize) {
        mAppSize = appSize;
    }

    public File[] getSubDirectory() {
        return mSubDirectoryPath;
    }

    public void setSubDirectory(File[] subDirectory) {
        mSubDirectoryPath = subDirectory;
    }
}
