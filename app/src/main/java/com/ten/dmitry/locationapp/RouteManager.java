package com.ten.dmitry.locationapp;

import java.util.HashMap;
import java.util.List;

public class RouteManager {
    private HashMap<String, BeaconRoute> routes;
    private BeaconRoute currentRoute;

    public RouteManager(List<BeaconRoute> newRoutes){
            routes = new HashMap<>();
        for(BeaconRoute route: newRoutes)
            routes.put(route.getName(), route);
    }

    public RouteManager(){
        routes = new HashMap<>();
    }

    public void addRoute(BeaconRoute newRoute){
        routes.put(newRoute.getName(), newRoute);
    }

    public void removeRoute(String routeName){
        for(int i=0; i<routes.size(); i++)
            if(routes.get(i).getName().equals(routeName)) {
                routes.remove(i);
                break;
            }
    }

    public BeaconRoute [] getRoutes(){
        BeaconRoute [] beaconRoutes = new BeaconRoute [routes.size()];
        int i=0;
        for(String name: routes.keySet())
            beaconRoutes[i] = routes.get(name);
        return beaconRoutes;
    }

    public BeaconRoute getRoute(String name){
        return routes.get(name);
    }

    public void setCurrentRoute(String name){
        currentRoute = routes.get(name);
    }

    public BeaconRoute getCurrentRoute(){
        return currentRoute;
    }
}
