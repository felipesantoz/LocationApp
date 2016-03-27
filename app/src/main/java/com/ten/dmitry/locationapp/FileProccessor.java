package com.ten.dmitry.locationapp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileProccessor {
    public static BeaconRoute constructRoute(
            Context context, String fileName, String routeName) {
        BeaconRoute route = new BeaconRoute(routeName);
        try {
            InputStream inputStream = context.openFileInput(fileName);

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
        return route;
    }

    public static void emptyFile(Context context, String fileName) {
        File dir = context.getFilesDir();
        File file = new File(dir, fileName);
        file.delete();
    }

    public static void deleteLine(Context context, String fileName, String routeName) {
        try {
            InputStream inputStream = context.openFileInput(fileName);
            ArrayList<String> tempStorage = new ArrayList<>();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    Log.d("TAG", receiveString);
                    tempStorage.add(receiveString);
                }
                inputStream.close();
            }
            int i;
            for (i=0; i<tempStorage.size(); i++)
                if (tempStorage.get(i).contains(routeName)) {
                    tempStorage.set(i, null);
                    break;
                }
            tempStorage.remove(i);
            emptyFile(context, fileName);
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.
                        openFileOutput(fileName, Context.MODE_APPEND));
                for (String line : tempStorage) {
                    outputStreamWriter.write(line + "\n");
                }
                outputStreamWriter.close();
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
}
