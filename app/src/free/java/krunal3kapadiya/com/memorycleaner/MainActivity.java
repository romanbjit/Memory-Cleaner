package krunal3kapadiya.com.memorycleaner;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.util.ArrayList;

import krunal3kapadiya.com.memorycleaner.adapter.AppListRVAdapter;
import krunal3kapadiya.com.memorycleaner.data.AppData;
import krunal3kapadiya.com.memorycleaner.widget.PieChart;

import static krunal3kapadiya.com.memorycleaner.Constants.getMimeType;

public class MainActivity extends AppCompatActivity
        implements AppListRVAdapter.OnItemClickListener {
    private final int PERMISSION_REQUEST_CODE = 1;
    ArrayList<AppData> mAppData;
    AppListRVAdapter adapter;
    File[] mFiles;
    File currentDir;

    private AdView mAdView;
    // [START_EXCLUDE]
    private InterstitialAd mInterstitialAd;
    private Button mLoadInterstitialButton;
    // [END_EXCLUDE]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        final PieChart pie = (PieChart) this.findViewById(R.id.Pie);

// TODO 1) Display dialog for app intro
        askForPermission();
        getHikeFolderSize();

        getTotalInternalMemorySize();
        getAvailableInternalMemorySize();

        pie.addItem("Hike Memory", hikeMemory, res.getColor(android.R.color.holo_orange_light));
        pie.addItem("WhatsApp Memory", whatsAppMemory, res.getColor(android.R.color.black));
        pie.addItem("Available Memory", avilableMemory, res.getColor(R.color.bluegrass));
        pie.addItem("Total Memory", totlaMemory - hikeMemory - whatsAppMemory - avilableMemory, res.getColor(R.color.slate));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAppData = new ArrayList<>();


        File whatsAppPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/");
        File hikeAppPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Hike/");

        mAppData.add(new AppData(whatsAppPath, whatsAppMemory, whatsAppPath.listFiles()));
        mAppData.add(new AppData(hikeAppPath, hikeMemory, hikeAppPath.listFiles()));

        File[] files = {whatsAppPath, hikeAppPath};

        adapter = new AppListRVAdapter(this, mFiles = files, this);

        recyclerView.setAdapter(adapter);

        TextView totalMemory = (TextView) findViewById(R.id.total_memory);
        TextView availableMemory = (TextView) findViewById(R.id.available_memory);
        TextView freeMemory = (TextView) findViewById(R.id.free_memory);
        totalMemory.setText("Total Memory" + formatSize(totlaMemory));
        availableMemory.setText("Free Memory" + formatSize(avilableMemory));
        freeMemory.setText("Around " + formatSize((hikeMemory + whatsAppMemory)) + " can be free up.");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // [END load_banner_ad]

        // AdMob ad unit IDs are not currently stored inside the google-services.json file.
        // Developers using AdMob can store them as custom values in a string resource file or
        // simply use constants. Note that the ad units used here are configured to return only test
        // ads, and should not be used outside this sample.

        // [START instantiate_interstitial_ad]
        // Create an InterstitialAd object. This same object can be re-used whenever you want to
        // show an interstitial.
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        // [END instantiate_interstitial_ad]

        // [START create_interstitial_ad_listener]
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
//                beginSecodActivity();
            }

            @Override
            public void onAdLoaded() {
                // Ad received, ready to display
                // [START_EXCLUDE]
                if (mLoadInterstitialButton != null) {
                    mLoadInterstitialButton.setEnabled(true);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onAdFailedToLoad(int i) {
                // See https://goo.gl/sCZj0H for possible error codes.
//                Log.w(TAG, "onAdFailedToLoad:" + i);
            }
        });
        // [END create_interstitial_ad_listener]

        // [START display_interstitial_ad]
//        mLoadInterstitialButton = (Button) findViewById(R.id.load_interstitial_button);
//        mLoadInterstitialButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
//                    beginSecondActivity();
//                }
//            }
//        });
        // [END display_interstitial_ad]

        // Disable button if an interstitial ad is not loaded yet.
//        mLoadInterstitialButton.setEnabled(mInterstitialAd.isLoaded());
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

    @Override
    public void onItemClick(int position) {
        currentDir = mFiles[position];


        if (currentDir.isDirectory()) {
            // TODO check length
            if (currentDir.canRead()) {
                adapter.setData(mFiles = currentDir.listFiles());
                adapter.notifyDataSetChanged();
            }
        } else {

            if (Intent.ACTION_GET_CONTENT.equals(getIntent().getAction())) {

                Intent intent = new Intent();

                intent.setDataAndType(Uri.fromFile(currentDir), getMimeType(currentDir));

                setResult(Activity.RESULT_OK, intent);

                finish();
            } else if (Constants.FileType.getFileType(currentDir) == Constants.FileType.ZIP) {

                final ProgressDialog dialog = ProgressDialog.show(this, "", "Unzipping", true);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                        setPath(unzip(currentDir));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.run();
            } else {

                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    intent.setDataAndType(Uri.fromFile(currentDir), getMimeType(currentDir));

                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void onDeleteClick(final int position) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mFiles[position].isDirectory()) {
                            String[] children = mFiles[position].list();
                            for (String aChildren : children) {
                                boolean delete = new File(mFiles[position], aChildren).delete();
                            }
                        } else {
                            boolean delete = mFiles[position].delete();
                        }
//                    mFiles[position].remove(position);
                        currentDir = mFiles[position];


                        if (currentDir.isDirectory()) {
                            // TODO check length
                            if (currentDir.canRead()) {
                                adapter.setData(mFiles = currentDir.listFiles());
                                adapter.notifyDataSetChanged();
                            }
                        }
//                                notifyItemChanged(getAdapterPosition());
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setMessage("Are you sure want to delete this file?")
                .create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (currentDir != null) {
            currentDir = currentDir.getParentFile();
            if (currentDir.getName().equals("0")) {
                File whatsAppPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/");
                File hikeAppPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Hike/");
                mFiles = new File[]{whatsAppPath, hikeAppPath};
            } else {
                mFiles = currentDir.listFiles();
            }
            if (mFiles != null) {
                refreshData();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void refreshData() {
        adapter.setData(mFiles);
        adapter.notifyDataSetChanged();
    }

    /**
     * Load a new interstitial ad asynchronously.
     */
    // [START request_new_interstitial]
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    // [START add_lifecycle_methods]

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        if (!mInterstitialAd.isLoaded()) {
            requestNewInterstitial();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
    // [END add_lifecycle_methods]

    @VisibleForTesting
    AdView getAdView() {
        return mAdView;
    }
}
