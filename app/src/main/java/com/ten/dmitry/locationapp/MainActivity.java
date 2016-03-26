package com.ten.dmitry.locationapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements BeaconManager.MonitoringListener, BeaconManager.RangingListener{
    private BeaconManager beaconManager;
    private Region beaconRegion;
    private int selectedMajor, selectedMinor;
    private final int REQUEST_BEACON_ACTIVITY_RESPONSE = 1;
    public final String TAG = "MainActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.setMonitoringListener(this);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        beaconManager.disconnect();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BEACON_ACTIVITY_RESPONSE && resultCode == Activity.RESULT_OK) {
            int[] ID = data.getIntArrayExtra(ChooseBeaconActivity.SELECTED_BEACON);
            selectedMajor = ID[0];
            selectedMinor = ID[1];
            Log.d(TAG, "" + selectedMajor + " " + selectedMinor);
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    Region region = new Region(
                            "monitored region",
                            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                            selectedMajor, selectedMinor);
                    beaconManager.startMonitoring(region);
                    beaconManager.startRanging(region);
                }
            });

        }
    }

    public void chooseBeacon(View view) {
        Intent intent = new Intent(this, ChooseBeaconActivity.class);
        startActivityForResult(intent, REQUEST_BEACON_ACTIVITY_RESPONSE);
    }

    @Override
    public void onEnteredRegion(Region region, List<Beacon> list) {
        for(Beacon b : list) {
            if(b.getMajor() == region.getMajor())
            showNotification(
                    "Beacon Found",
                    "Major: " + b.getMajor());
        }
    }

    @Override
    public void onExitedRegion(Region region) {
        showNotification("Beacon lost", "");
    }

    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> list){

    }
}
