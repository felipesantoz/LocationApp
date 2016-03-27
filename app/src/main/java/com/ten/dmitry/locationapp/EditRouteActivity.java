package com.ten.dmitry.locationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EditRouteActivity extends AppCompatActivity {
    private String routeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route);
        TextView display = (TextView)findViewById(R.id.command_list_for_admin);
        routeName = getIntent().getStringExtra(ManageRouteActivity.SELECTED_ROUTE_ADMIN);
        display.setText(getDirections(routeName));
    }

    private String getDirections(String routeName) {
        BeaconRoute route = new BeaconRoute(routeName);
        try {
            InputStream inputStream = openFileInput("beaconsRecorded.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    String[] split1 = receiveString.split("\\.");
                    String name = split1[1];
                    if (name.equals(routeName)) {
                        List<Integer> majorList = new ArrayList<>();
                        String[] split2 = split1[0].split(",");
                        String[] messages = new String[split2.length];
                        for (int i = 0; i < split2.length; i++) {
                            String[] split3 = split2[i].split("/");
                            majorList.add(Integer.parseInt(split3[0]));
                            messages[i] = split3[1];
                        }
                        for (int i = 0; i < majorList.size(); i++)
                            route.addBeacon(majorList.get(i), messages[i]);
                        break;
                    }
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return route.toString();
    }

    public void deleteRoute(View view){
        FileProccessor.deleteLine(this, "beaconsRecorded.txt", routeName);
        finish();
    }

    public void closeActivity(View view){
        finish();
    }
}
