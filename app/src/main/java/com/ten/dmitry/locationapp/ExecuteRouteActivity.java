package com.ten.dmitry.locationapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

public class ExecuteRouteActivity extends AppCompatActivity implements BeaconManager.RangingListener{
    private BeaconManager beaconManager;
    private BeaconRoute route;
    private Integer nextMajor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_route);


        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setRangingListener(this);

        RouteManager routeManager = new RouteManager();
        route = routeManager.getRoute(getIntent().getStringExtra(ChooseRouteActivity.SELECTED_ROUTE));
        nextMajor = route.getNextBeaconMajor();
    }

    public void onStart(){
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
    }

    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
        for (Beacon b : list) {
            if (b.getMajor() == nextMajor) {
                if (calculateAccuracy(b.getMeasuredPower(), b.getRssi()) < 1.8) {
                    showNotification("Beacon Message", route.getMessage(nextMajor));
                    if (!route.hasNext()) {
                        this.finish();
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
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }

}