package com.ten.dmitry.locationapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BaseRouteActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> routeNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_route);
        routeNames = new ArrayList<>();

        // Setting up a list view
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, R.layout.list_view_item, routeNames);
        listView.setAdapter(adapter);
    }

}
