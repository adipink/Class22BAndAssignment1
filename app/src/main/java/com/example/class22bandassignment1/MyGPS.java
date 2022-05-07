package com.example.class22bandassignment1;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class MyGPS extends Service implements LocationListener {
    // The minimum distance to change Updates in meters // 10 meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // The minimum time between updates in milliseconds // 1 minute
    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;

    private final Context myContext;

    //check is GPS option is turned on
    private boolean isEnabled;

    //check is there is internet
    private boolean isInternet;

    //check if GPS can get location
    private boolean isGPSWorking;

    private Location myLocation;
    private double lati;
    private double longi;

    protected LocationManager locationManager;


    public MyGPS(Context context) {
        myContext = context;
        isEnabled = false;
        isInternet = false;
        isGPSWorking = false;
        getMyLocation();
    }

    private Location getMyLocation() {
        try {
            locationManager = (LocationManager) myContext.getSystemService(LOCATION_SERVICE);

            //save if GPS enabled
            isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //save if internet Enabled
            isInternet = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //check if GPS and internet are Enabled
            if(!isEnabled && !isInternet){ //Disabled

            }else{// Enabled
               isGPSWorking = true;
               //get location from Network Provider
                if(isInternet){
                    //check network permission
                    if(ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(myContext,Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
                        //request permission
                        ActivityCompat.requestPermissions((Activity) myContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},101);
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this);

                    //Log.d("Network", "Network");
                    if(locationManager != null){
                        myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(myLocation != null){
                            lati = myLocation.getLatitude();
                            longi = myLocation.getLongitude();
                        }
                    }
                }

                //get location using GPS
                if (isEnabled){
                    if(myLocation == null){
                        //check network permission
                        if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //request permission
                            ActivityCompat.requestPermissions((Activity) myContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                this);
                         Log.d("GPS Enabled","GPS Enabled");

                         if(locationManager != null){
                             myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                             if(myLocation != null){
                                 lati = myLocation.getLatitude();
                                 longi = myLocation.getLongitude();
                             }
                         }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return myLocation;


    }


    //stop using GPS
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(MyGPS.this);
        }
    }

    public double geyMyLat(){
        if(myLocation != null){
            lati = myLocation.getLatitude();
        }
        return lati;
    }

    public double getMyLongi(){
        if(myLocation != null){
            longi = myLocation.getLongitude();
        }
        return longi;
    }

    public boolean getIsGPSWorking() {
        return isGPSWorking;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(myContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                myContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}
