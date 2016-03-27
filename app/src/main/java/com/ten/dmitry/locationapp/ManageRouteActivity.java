package com.ten.dmitry.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ManageRouteActivity extends BaseRouteActivity {

    public static final String SELECTED_ROUTE_ADMIN = "ManageRouteActivityRoute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_route);
    }

    public void onListItemSelected(View view) {
        String selectedRouteName = super.getSelectedRouteName(view);
        Intent startRouteIntent = new Intent(this, EditRouteActivity.class);
        startRouteIntent.putExtra(SELECTED_ROUTE_ADMIN, selectedRouteName);
        startActivity(startRouteIntent);
    }

    public void createNewTask(View view) {
        Intent createRouteIntent = new Intent(this, RouteNameActivity.class);
        startActivity(createRouteIntent);
    }
}
