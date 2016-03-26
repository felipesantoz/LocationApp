package com.ten.dmitry.locationapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/*
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;*/

public class ExecuteRouteActivity extends AppCompatActivity implements BeaconManager.RangingListener {
    private BeaconManager beaconManager;
    private BeaconRoute route;
    private Integer nextMajor;
    TextToSpeech ttsObject;
    int result;
    final static String TAG = "RouteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_route);


        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setRangingListener(this);
        String routeName = getIntent().getStringExtra(ChooseRouteActivity.SELECTED_ROUTE_USER);
        route = new BeaconRoute(routeName);
        try {
            InputStream inputStream = openFileInput("Routes.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    String[] split1 = receiveString.split("\\.");
                    String name = split1[1];
                    List<Integer> majorList = new ArrayList<>();
                    String[] split2 = split1[0].split(",");
                    String[] messages = new String[split2.length];
                    for (int i = 0; i < split2.length; i++) {
                        String[] split3 = split2[i].split("/");
                        majorList.add(Integer.parseInt(split3[0]));
                        messages[i] = split3[1];
                    }
                    if (name.equals(routeName))
                        for (int i = 0; i < majorList.size(); i++)
                            route.addBeacon(majorList.get(i), messages[i]);
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        nextMajor = route.getNextBeaconMajor();


        ttsObject = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS)
                    result = ttsObject.setLanguage(Locale.US);

                else
                    Toast.makeText(getApplicationContext(), "Feature Not Supported in Your Device", Toast.LENGTH_SHORT).show();


            }
        });
    }

    public void onStart() {
        super.onStart();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                Region region = new Region(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        null, null);
                beaconManager.startRanging(region);
            }
        });
    }

    public void onResume() {
        super.onResume();
        Region region = new Region(
                "monitored region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                null, null);
        beaconManager.startRanging(region);
    }

    /*
    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }*/

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
        Log.d(TAG, "It works");
        for (Beacon b : list) {
            if (b.getMajor() == nextMajor) {
                if (calculateAccuracy(b.getMeasuredPower(), b.getRssi()) < 1) {
                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
                        Toast.makeText(getApplicationContext(), "Feature Not Supported in Your Device", Toast.LENGTH_SHORT).show();
                    else {
                        ttsObject.speak(route.getMessage(nextMajor), TextToSpeech.QUEUE_FLUSH, null, route.getName() + "." + nextMajor);

                    }
                    if (!route.hasNext()) {

                    } else
                        nextMajor = route.getNextBeaconMajor();
                }
            }
        }
    }

    protected static double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
        }
    }

}
