package com.ten.dmitry.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

public class MainActivity extends AppCompatActivity {
    private BeaconManager beaconManager;
    private RouteManager routeManager;
    private Region beaconRegion;
    private int selectedMajor, selectedMinor;
    private final int REQUEST_BEACON_ACTIVITY_RESPONSE = 1;
    public final String TAG = "MainActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onDestroy() {
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


    /**
     * Called, when User button is clicked. Starts the intent to the user part of the app.
     */
    public void userModeSelected(View view) {
        Intent chooseRouteIntent = new Intent(this, ChooseRouteActivity.class);
        startActivity(chooseRouteIntent);
    }

    /**
     * Called, when Admin button is clicked. Starts the intent to the administrator part of the app.
     */
    public void adminModeSelected(View view) {
        Intent manageRouteIntent = new Intent(this, ManageRouteActivity.class);
        startActivity(manageRouteIntent);
    }
}
