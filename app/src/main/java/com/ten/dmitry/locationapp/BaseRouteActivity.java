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
import java.util.List;

public class BaseRouteActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> routeNames;
    public String TAG = "BaseRouteActivity";

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

    private void fillListFromFile(){
        String ret = "";
        try {
            InputStream inputStream = openFileInput("Routes.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    Log.d(TAG, receiveString);
                    String[] split1 = receiveString.split("\\.");
                    String name = split1[1];
                    List<Integer> majorList = new ArrayList<>();
                    String[] split2 = split1[0].split(",");
                    String[] messages = new String[split2.length];
                    for (int i = 0; i < split2.length; i++) {
                        String[] split3 = split2[i].split("/");
                        majorList.add(Integer.parseInt(split3[0]));
                        messages[i] = split3[1];
                    }
                    int i;
                    for (i = 0; i < adapter.getCount(); i++)
                        if (adapter.getItem(i).equals(name))
                            break;
                    if (i == adapter.getCount())
                        adapter.add(name);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        Log.d(TAG, ret);
    }
}
