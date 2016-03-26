package com.ten.dmitry.locationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;

public class CreateRouteActivity extends AppCompatActivity implements BeaconManager.RangingListener{

    BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setRangingListener(this);

    }

    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
        Beacon b = list.get(0);
        if (calculateAccuracy(b.getMeasuredPower(), b.getRssi()) < 0.3){
            Intent intent = new Intent(this, ChooseRouteOptionActivity.class);
            startActivity(intent);
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
