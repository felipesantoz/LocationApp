package com.ten.dmitry.locationapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class ManageRouteActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private SharedPreferences appData;
    public static final String SELECTED_ROUTE_ADMIN = "ManageRouteActivityRoute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_route);
        ArrayList<String> routeNames = new ArrayList<>();
        // Two options: 1. No saved routes. 2. There are saved routes.
        // Getting hold of activity`s preference file.
        appData = this.getPreferences(Context.MODE_PRIVATE);
        HashMap map = (HashMap) appData.getAll();
        if(!(map.size() == 0)) {
            Set key_set = map.keySet();
            for (Object key : key_set)
                routeNames.add((String)key);
            Collections.sort(routeNames);
        }

        // populating the list view with the possible route names
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, R.layout.list_view_item, routeNames);
        listView.setAdapter(adapter);
    }

    public void listItemClick(View view) {
        int pos = listView.getPositionForView(view);
        String selectedRouteName = adapter.getItem(pos);
        Intent startRouteIntent = new Intent(this, EditRouteActivity.class);
        startRouteIntent.putExtra(SELECTED_ROUTE_ADMIN, selectedRouteName);
        startActivity(startRouteIntent);
    }

    public void createNewTask(View view){
        Intent createRouteIntent = new Intent(this, CreateRouteActivity.class);
        startActivity(createRouteIntent);
    }
}
