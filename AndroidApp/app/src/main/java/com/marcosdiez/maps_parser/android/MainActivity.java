package com.marcosdiez.maps_parser.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.marcosdiez.maps_parser.R;
import com.marcosdiez.maps_parser.parser.OnlineParser;

import java.io.IOException;


public class MainActivity extends Activity {
    final static String TAG = "MAPS_PARSER";
    final static String yadex_package_name = "ru.yandex.yandexmaps";
    public static OnlineParser theData = null;
    public static Activity theActivity = null;
    TextView textViewId = null;

    public static void showExportDialog(final Activity theActivity) {
        // final Activity activityCopy = theActivity;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(theActivity);
        alertDialog.setMessage(R.string.choose_your_maps_app)
                .setPositiveButton(R.string.google_maps, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = buildAndroidMapsUri(theData.getLatitude(), theData.getLongitude());
                        openUrl(url, theActivity);
                        theActivity.finish();
                    }
                })
                .setNegativeButton(R.string.yandex_maps, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = buildYandexMapsUrl(theData.getLatitude(), theData.getLongitude());
                        openUrl(url, theActivity, yadex_package_name);
                        theActivity.finish();
                    }
                });

        alertDialog.create().show();
    }

    static String buildAndroidMapsUri(String latitude, String longitude) {
        String theURL = "geo:0,0?q=" + latitude + "," + longitude;
        return theURL;
    }

    static String buildYandexMapsUrl(String latitude, String longitude) {
        // http://maps.yandex.com/?rtext=59.326460%2C12.699889~57.386826%2C12.348327&sll=12.941588%2C56.517713&sspn=34.716797%2C12.029728&rtm=atm&source=route&ll=12.941588%2C56.517713&spn=34.716797%2C12.029728&z=6&l=map
        // https://maps.yandex.com/?rtext=~45.829453,15.978298
        String theURL = "https://maps.yandex.com/?rtext=~" + latitude + "," + longitude;
        return theURL;
    }

    static void openUrl(String theURL, Activity theActivity) {
        openUrl(theURL, theActivity, null);
    }

    static void openUrl(String theURL, Activity theActivity, String thePackage) {
        Log.d(TAG, "Opening location:" + theURL);
        final Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(theURL));
        if (thePackage != null) {
            intent.setPackage(thePackage);
        }
        theActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        // -23.548943,-46.638818
        // -23.545751,-46.66941
        //openGpsUrl("-23.545751" , "-46.66941");
        // ParsedIntelUrlTest.doTests();

        processIntent();
        //
    }

    private void processIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(this, R.string.intent_not_received, Toast.LENGTH_SHORT).show();
            return;
        }
        String data = intent.getDataString();
        if (data == null) {
            Toast.makeText(this, R.string.intent_contains_not_data, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "Received URL:" + data);
        try {
            OnlineParser onlineParser = new OnlineParser();
            onlineParser.parseOffline(data);

            if (onlineParser.hasValue()) {
                openGpsUrl(onlineParser);
            } else {
                processInternetIntent(onlineParser);
            }
        } catch (IOException m) {
            Toast.makeText(this, "Invalid URL " + data, Toast.LENGTH_SHORT).show();
        }
    }

    void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    void processInternetIntent(OnlineParser theDataX) {
        setContentView(R.layout.activity_main);
        textViewId = ((TextView) findViewById(R.id.textview_id));
        textViewId.setText(theDataX.getUrl());
        toast("Please Wait. Resolving URL...");
        new ProcessUrlInBackground().execute(theDataX);

    }

    void openGpsUrl(OnlineParser theDataX) {
        Log.d(TAG, yadex_package_name + ": " + (appInstalledOrNot(yadex_package_name) ? "true" : "false"));

        if (appInstalledOrNot(yadex_package_name)) {
            MainActivity.theData = theDataX;
            MainActivity.theActivity = this;
            showExportDialog(this); // Yandex requires a different intent
        } else {
            // google maps
            String url = buildAndroidMapsUri(theDataX.getLatitude(), theDataX.getLongitude());
            openUrl(url, this);
            finish();
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    class ProcessUrlInBackground extends AsyncTask<OnlineParser, Void, OnlineParser> {
        private OnlineParser theOnlineParser = null;
        private boolean worked = false;

        @Override
        protected OnlineParser doInBackground(OnlineParser... theDataX) {
            try {
                theOnlineParser = theDataX[0];
                theOnlineParser.parseOnline();
                worked = true;
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(OnlineParser theDataX) {
            if (worked && theOnlineParser != null && theOnlineParser.hasValue()) {
                ((TextView) findViewById(R.id.textview_result)).setText(theOnlineParser.getUrl());
                openGpsUrl(theOnlineParser);
            } else {
                textViewId.setText("Error resolving URL: " + theOnlineParser.getUrl());
            }
        }
    }
}
