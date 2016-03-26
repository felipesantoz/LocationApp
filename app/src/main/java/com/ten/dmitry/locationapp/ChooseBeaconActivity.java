package com.ten.dmitry.locationapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.List;

public class ChooseBeaconActivity extends AppCompatActivity implements BeaconManager.RangingListener{

    private List<Beacon> beaconList;
    private List<Button> buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // find this line inside the `onCreate` method:
        BeaconManager beaconManager = new BeaconManager(this);
        // add this below:
        beaconManager.setRangingListener(this);
    }

    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
        buttonList = new ArrayList<>();
        RelativeLayout myLayout = new RelativeLayout(this);

        for (Beacon b : list){
            Button btn = new Button(this);
            btn.setText(b.getProximityUUID().toString());
            myLayout.addView(btn);
        }

        setContentView(myLayout);

    }
}
