package krunal3kapadiya.com.memorycleaner.data;

/**
 * Created by sanjay on 8/22/2017.
 */

public class AppData {
    private String mAppName;
    private long mAppSize;

    public AppData(String appName, long appSize) {
        mAppName = appName;
        mAppSize = appSize;
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
}
