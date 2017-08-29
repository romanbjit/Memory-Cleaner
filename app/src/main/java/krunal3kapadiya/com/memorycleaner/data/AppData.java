package krunal3kapadiya.com.memorycleaner.data;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Krunal on 8/22/2017.
 */

public class AppData {
    private File mAppName;
    private long mAppSize;
    private File[] mSubDirectoryPath;

    public AppData(File appName, long appSize, File[] files) {
        mAppName = appName;
        mAppSize = appSize;
        mSubDirectoryPath = files;
    }

    public File getAppName() {
        return mAppName;
    }

    public void setAppName(File appName) {
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
