package com.ten.dmitry.locationapp;

import android.content.Intent;
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

public class ManageRouteActivity extends BaseRouteActivity {

    public static final String SELECTED_ROUTE_ADMIN = "ManageRouteActivityRoute";
    public static final String TAG = "ManageRoute:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void listItemClick(View view) {
        int pos = listView.getPositionForView(view);
        String selectedRouteName = adapter.getItem(pos);
        Intent startRouteIntent = new Intent(this, EditRouteActivity.class);
        startRouteIntent.putExtra(SELECTED_ROUTE_ADMIN, selectedRouteName);
        startActivity(startRouteIntent);
    }

    public void createNewTask(View view) {
        Intent createRouteIntent = new Intent(this, CreateRouteActivity.class);
        startActivity(createRouteIntent);
    }

    public void onResume() {
        super.onResume();
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
