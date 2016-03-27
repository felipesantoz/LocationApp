package com.ten.dmitry.locationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class RouteNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_name);


    }

    public void goToNextStep(View view){
        Intent createRouteIntent = new Intent(this, CreateRouteActivity.class);
        startActivity(createRouteIntent);
    }


    public void finishCreatingRoute(View view){
        EditText routeNameEditor = (EditText)findViewById(R.id.route_name_input);
        String name = routeNameEditor.getText().toString();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("Routes.txt", Context.MODE_APPEND));
            outputStreamWriter.write("." + name + "\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        finish();
    }
}
