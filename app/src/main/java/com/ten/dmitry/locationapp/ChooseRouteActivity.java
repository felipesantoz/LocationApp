package com.ten.dmitry.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseRouteActivity extends BaseRouteActivity{
    public static final String SELECTED_ROUTE_USER = "ChooseRouteActivityRoute";
    public static final String CHOOSE_ROUTE_TAG = "ChooseRouteActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void onListItemSelected(View view) {
        String selectedRouteName = super.getSelectedRouteName(view);
        Intent startRouteIntent = new Intent(this, ExecuteRouteActivity.class);
        startRouteIntent.putExtra(SELECTED_ROUTE_USER, selectedRouteName);
        startActivity(startRouteIntent);
    }
}

