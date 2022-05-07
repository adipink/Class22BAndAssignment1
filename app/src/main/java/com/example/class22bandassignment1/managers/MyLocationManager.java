package com.example.class22bandassignment1.managers;

import android.content.Context;

import com.example.class22bandassignment1.MyGPS;

public class MyLocationManager {

    private MyGPS gps;

    public MyLocationManager(Context context){
        gps = new MyGPS(context);
    }

    public double getLat(){
        double lat = 0.0;
        if(gps.getIsGPSWorking()) {
            lat = gps.geyMyLat();
        }else{
            gps.showSettingsAlert();
            lat = 0.0;
        }
        return lat;
    }

    public double getLongi(){
        double lon = 0.0;
        if(gps.getIsGPSWorking()){
            lon = gps.getMyLongi();
        }else{
            lon = 0.0;
        }
        return lon;
    }

}
