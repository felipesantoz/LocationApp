package com.ten.dmitry.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ChooseRouteActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    public static final String SELECTED_ROUTE = "ChooseRouteActivityRoute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_route2);
        ArrayList<String> routeNames = new ArrayList<>();
        // TODO: Extract possible route names from the saved data in the admin section

        // populating the list view with thw possible route names

        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, R.layout.list_view_item, routeNames);
        listView.setAdapter(adapter);
    }

    public void listItemClick(View view) {
        int pos = listView.getPositionForView(view);
        String selectedRouteName = adapter.getItem(pos);
        Intent startRouteIntent = new Intent(this, ExecuteRouteActivity.class);
        startRouteIntent.putExtra(SELECTED_ROUTE, selectedRouteName);
        startActivity(startRouteIntent);
    }
}
