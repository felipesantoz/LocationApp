package com.ten.dmitry.locationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.UUID;

public class CreateRouteActivity extends AppCompatActivity implements BeaconManager.RangingListener {

    private BeaconManager beaconManager;
    private Region searchRegion;
    private Beacon lastBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

        // initializing beacon manager and listener
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setRangingListener(this);

    }

    public void onResume() {
        super.onResume();
        // starting looking for all beacons in the range
        searchRegion = new Region(
                "monitored region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                null, null);
        beaconManager.startRanging(searchRegion);
    }

    public void onPause() {
        super.onPause();
        beaconManager.stopRanging(searchRegion);
    }

    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
        // the beacon, that is considered to be closest to the device is selected.
        Beacon b = list.get(0);
        // setting up directions for the beacon, if it is close enough
        if (calculateAccuracy(b.getMeasuredPower(), b.getRssi()) < 0.3 && !b.equals(lastBeacon)) {
            Intent intent = new Intent(this, ChooseRouteOptionActivity.class);
            intent.putExtra("MAJOR", b.getMajor());
            lastBeacon = b;
            startActivity(intent);
        } else if (!(calculateAccuracy(b.getMeasuredPower(), b.getRssi()) < 0.3) && b.equals(lastBeacon))
            lastBeacon = null;

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

    /**
     * onClick method of the "Done" button. Saves the name of the route to the file and finishes
     * creating a new route.
     *
     * @param view the view that was activated. Button in this case.
     */
    public void finishCreatingRoute(View view) {
        EditText routeNameEditor = (EditText) findViewById(R.id.route_name_input);
        String name = routeNameEditor.getText().toString();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("Routes.txt", Context.MODE_APPEND));
            outputStreamWriter.write("." + name + "\n");
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        finish();
    }
}
