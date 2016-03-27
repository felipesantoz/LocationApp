package com.ten.dmitry.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class ManageRouteActivity extends BaseRouteActivity {

    public static final String SELECTED_ROUTE_ADMIN = "ManageRouteActivityRoute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button createRoute = (Button)findViewById(R.id.create_route_btn);
        createRoute.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ArrayAdapter)getListView().getAdapter()).notifyDataSetChanged();
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
