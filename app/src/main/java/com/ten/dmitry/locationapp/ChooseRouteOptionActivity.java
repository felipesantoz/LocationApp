package com.ten.dmitry.locationapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class ChooseRouteOptionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private String choice;
    private int major;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_route_option);
        TextView label = (TextView) findViewById(R.id.major_label);
        major = getIntent().getIntExtra("MAJOR", -1);
        label.setText(label.getText() + Integer.toString(major));
        ListView listView;
        listView = (ListView) findViewById(R.id.routeOptionListView);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String message = ((TextView) view).getText().toString();
        if (!message.equals("Cancel")) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.
                        openFileOutput("beaconsRecorded.txt", Context.MODE_APPEND));
                outputStreamWriter.write(major + "/" + message + ",");
                outputStreamWriter.close();
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }
        finish();
    }
}
