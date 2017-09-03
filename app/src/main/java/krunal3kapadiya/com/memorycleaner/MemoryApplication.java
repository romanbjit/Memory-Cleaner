package krunal3kapadiya.com.memorycleaner;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Krunal on 9/3/2017.
 */

public class MemoryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

    }
}
