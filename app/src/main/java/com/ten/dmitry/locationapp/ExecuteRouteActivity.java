package com.ten.dmitry.locationapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ExecuteRouteActivity extends AppCompatActivity implements BeaconManager.RangingListener {
    private BeaconManager beaconManager;
    private Region rangeRegion = null;
    private BeaconRoute route;
    private boolean detachedHasNext;
    private Integer nextMajor, currentMajor;
    private TextToSpeech ttsObject;
    private TextView textView;
    private int result;
    final static String EXECUTE_ROUTE_TAG = "ExecuteRouteActivityTag";

    // Instance of class Handler that the queue of processes, used to start the Runnable object
    // and to make it call itself indefinitely.
    private final Handler handler = new Handler();

    // A to-be-thread object, the only purpose - start a new thread that will call speech command.
    private final Runnable loadRunner = new Runnable() {
        @Override
        public void run() {
            final Thread speech_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    speak(currentMajor);
                }
            });
            speech_thread.start();
        }
    };

    private final Runnable finishRunner = new Runnable(){
        @Override
        public void run(){
            final Thread speech_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
            speech_thread.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_route);

        // initializing beacon manager with listeners.
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setRangingListener(this);

        // initializing the route to be executed.
        String routeName = getIntent().getStringExtra(ChooseRouteActivity.SELECTED_ROUTE_USER);
        initRoute(routeName);

        textView = (TextView)findViewById(R.id.command_list);

        // initializing text to speech program.
        ttsObject = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS)
                    result = ttsObject.setLanguage(Locale.US);

                else
                    Toast.makeText(getApplicationContext(), "Feature Not Supported in Your Device", Toast.LENGTH_SHORT).show();


            }
        });
    }

    /**
     * Initializes the route from the data storage, by finding all of the neccessary data from route name
     *
     * @param routeName the route name to be accessed
     */
    private void initRoute(String routeName) {
        route = FileProccessor.constructRoute(this, "beaconsRecorded.txt", routeName);
        nextMajor = route.getNextBeaconMajor();
    }

    public void onResume() {
        super.onResume();
        startRangingForBeacon(nextMajor);
    }

    private void startRangingForBeacon(int major) {
        rangeRegion = new Region(
                "monitored region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                major, null);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(rangeRegion);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.stopRanging(rangeRegion);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
        for (Beacon b : list) {
            if (b.getMajor() == nextMajor) {
                if (calculateAccuracy(b.getMeasuredPower(), b.getRssi()) < 1.5) {
                    textView.setText(route.getMessage(nextMajor));
                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
                        Toast.makeText(getApplicationContext(), "Feature Not Supported in Your Device",
                                Toast.LENGTH_SHORT).show();
                    else {
                        currentMajor = nextMajor;
                        detachedHasNext = route.hasNext();
                        handler.post(loadRunner);
                    }
                    if (route.hasNext()) {
                        nextMajor = route.getNextBeaconMajor();
                        startRangingForBeacon(nextMajor);
                    }
                    else{
                        nextMajor = -1;
                        handler.postDelayed(finishRunner, 2000);
                    }
                }
            }
        }
    }

    protected static double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void speak(int currentMajor) {
        ttsObject.speak(route.getMessage(currentMajor), TextToSpeech.QUEUE_FLUSH,
                null, route.getName() + "." + currentMajor);
        handler.removeCallbacks(loadRunner);
    }
}
