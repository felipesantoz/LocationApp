package com.ten.dmitry.locationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BaseRouteActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> routeNames;
    public String BASE_ROUTE_TAG = "BaseRouteActivity";

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
    @Override
    public void onResume() {
        super.onResume();
        fillListFromFile();
    }

    public String getSelectedRouteName(View view){
        int pos = listView.getPositionForView(view);
        String selectedRouteName = adapter.getItem(pos);
        return selectedRouteName;
    }

    public void fillListFromFile(){
        String ret = "";
        try {
            InputStream inputStream = openFileInput("Routes.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    Log.d(BASE_ROUTE_TAG, receiveString);
                    String[] split1 = receiveString.split("\\.");
                    String name = split1[1];
                    int i;
                    for (i = 0; i < adapter.getCount(); i++)
                        if (adapter.getItem(i).equals(name))
                            break;
                    if (i == adapter.getCount())
                        adapter.add(name);
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
}
