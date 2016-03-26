package com.ten.dmitry.locationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChooseBeaconActivity extends AppCompatActivity implements BeaconManager.RangingListener {
    public static String SELECTED_BEACON;
    private BeaconManager beaconManager;
    private Region region;
    private List<Beacon> beaconSet;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    public static final String TAG = "ChooseBeaconActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_beacon);

        // find this line inside the `onCreate` method:
        beaconManager = new BeaconManager(this);
        region = new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
        // add this below:
        beaconManager.setRangingListener(this);

        beaconSet = new ArrayList<>();
        ArrayList<String> beaconsIds = new ArrayList<>();
        beaconsIds.add("");
        listView = (ListView) findViewById(R.id.list_view);
        listView.getHeight();
        adapter = new ArrayAdapter<>(this, R.layout.list_view_item, beaconsIds);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
        Log.d(TAG, String.valueOf(list.size()));

        for (Beacon b : list) {
            if (!beaconSet.contains(b)) {
                adapter.add(String.valueOf(b.getMajor()));
                beaconSet.add(b);
            }
        }

    }

    public void listItemClick(View view) {
        int pos = listView.getPositionForView(view);
        Beacon beaconSelected = beaconSet.get(pos);
        int [] beaconID = {beaconSelected.getMajor(), beaconSelected.getMinor()};
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SELECTED_BEACON, beaconID);
        setResult(Activity.RESULT_OK, resultIntent);
        this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.stopRanging(region);
    }
}
