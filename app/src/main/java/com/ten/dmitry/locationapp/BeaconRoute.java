package com.ten.dmitry.locationapp;

import java.util.ArrayList;
import java.util.List;

public class BeaconRoute {
    private List<Object []> beaconMessages;
    private String name;
    private int currentMajorIndex = 0;


    public BeaconRoute(String name, List<Integer> beaconMajors, String[] messages) {
        this.name = name;
        beaconMessages = new ArrayList<>();
        int i = 0;
        for (Integer bM : beaconMajors) {
            Object [] BeaconInfo = {bM, messages[i]};
            beaconMessages.add(BeaconInfo);
            i++;
        }
    }

    public BeaconRoute(String name){
        this.name = name;
        beaconMessages = new ArrayList<>();
    }

    public void addBeacon(Integer beaconMajor, String message) {
        Object [] BeaconInfo = {beaconMajor, message};
        beaconMessages.add(BeaconInfo);
    }

    public String getMessage(Integer beaconMajor) {
        for(Object [] beaconInfo: beaconMessages)
            if(((Integer)beaconInfo[0]).equals(beaconMajor))
                return (String)beaconInfo[1];
        throw new NullPointerException();
    }

    public String getName(){
        return name;
    }

    public void setName(String newName){
        name = newName;
    }

    public int getNextBeaconMajor(){
        int major = (Integer)beaconMessages.get(currentMajorIndex)[0];
        currentMajorIndex++;
        return major;
    }

    public boolean hasNext(){
        return currentMajorIndex < beaconMessages.size();
    }
}
