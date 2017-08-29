package krunal3kapadiya.com.memorycleaner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import krunal3kapadiya.com.memorycleaner.adapter.AppListRVAdapter;
import krunal3kapadiya.com.memorycleaner.data.AppData;
import krunal3kapadiya.com.memorycleaner.widget.PieChart;

public class MainActivity extends AppCompatActivity {
    private final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        final PieChart pie = (PieChart) this.findViewById(R.id.Pie);


        askForPermission();
        getHikeFolderSize();

        pie.addItem("Hike Memory", hikeMemory, res.getColor(android.R.color.holo_orange_light));
        pie.addItem("WhatsApp Memory", whatsAppMemory, res.getColor(android.R.color.black));
        pie.addItem("Available Memory", avilableMemory, res.getColor(R.color.bluegrass));
        pie.addItem("Total Memory", totlaMemory - hikeMemory - whatsAppMemory - avilableMemory, res.getColor(R.color.slate));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<AppData> mAppData = new ArrayList<>();

        File whatsAppPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/");
        File hikeAppPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Hike/");


        mAppData.add(new AppData("WhatsApp", whatsAppMemory, whatsAppPath.listFiles()));
        mAppData.add(new AppData("Hike", hikeMemory, hikeAppPath.listFiles()));

        AppListRVAdapter adapter = new AppListRVAdapter(this, mAppData);

        recyclerView.setAdapter(adapter);

        TextView totalMemory = (TextView) findViewById(R.id.total_memory);
        TextView availableMemory = (TextView) findViewById(R.id.available_memory);
        TextView freeMemory = (TextView) findViewById(R.id.free_memory);
        totalMemory.setText("Total Memory" + formatSize(totlaMemory));
        availableMemory.setText("Free Memory" + formatSize(avilableMemory));
        freeMemory.setText("Around " + formatSize(hikeMemory + whatsAppMemory) + " can be free up.");
    }

    private String getHikeFolderSize() {
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Hike/");
        String value;
        long Filesize = getFolderSize(path) / 1024;//call function and convert bytes into Kb
        hikeMemory = Filesize;
        if (Filesize >= 1024)
            value = Filesize / 1024 + " Mb";
        else
            value = Filesize + " Kb";
        return value;
    }

    long avilableMemory;
    long totlaMemory;

    public String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        avilableMemory = availableBlocks * blockSize;
        return formatSize(availableBlocks * blockSize);
    }

    public String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        totlaMemory = totalBlocks * blockSize;
        return formatSize(totalBlocks * blockSize);
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
                if (size >= 1024) {
                    suffix = "GB";
//                    size /= 1048576;
                    size /= 1024;
                }
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    private void askForPermission() {
        // list of permissions
        String[] permission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // checking permissions, if granted or not
        if ((ContextCompat.checkSelfPermission(this, permission[0])) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);
        } else {
            getWhatsAppFolderSize();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO set the default permission task
                    getWhatsAppFolderSize();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    long whatsAppMemory;
    long hikeMemory;

    private String getWhatsAppFolderSize() {
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/");
        String value;
        long Filesize = getFolderSize(path) / 1024;//call function and convert bytes into Kb
        whatsAppMemory = Filesize;
        if (Filesize >= 1024)
            value = Filesize / 1024 + " Mb";
        else
            value = Filesize + " Kb";
        return value;
    }

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
