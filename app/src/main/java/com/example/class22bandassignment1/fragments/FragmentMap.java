package com.example.class22bandassignment1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.example.class22bandassignment1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentMap extends Fragment {
   private GoogleMap googleMap;


   @Override
   public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
      //Initialize view
      View view = inflater.inflate(R.layout.fragment_map, container, false);
      //Initialize map fragment
      SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

      //Async map
      supportMapFragment.getMapAsync(new OnMapReadyCallback() {
         @Override
         public void onMapReady(@NonNull GoogleMap googleMap) {
            //when map is loaded
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
               @Override
               public void onMapClick(@NonNull LatLng latLng) {
                  //when clicked on map
                  //Initialize marker options
                  MarkerOptions markerOptions = new MarkerOptions();
                  //set position of marker
                  markerOptions.position(latLng);
                  //set title of marker
                  markerOptions.title(latLng.latitude+ " : "+latLng.longitude);
                  //remove all marker
                  googleMap.clear();
                  //animationg to zoom the marker
                  googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                  //add marker on map
                  googleMap.addMarker(markerOptions);
               }
            });
         }
      });
      //return view
      return view;
   }

   public GoogleMap getGoogleMap() {
      return googleMap;
   }

   public void locateOnMap(double x, double y) {
      if (x==0.0 && y==0.0) {
         //locationDetails.setVisibility(View.VISIBLE);
         //locationDetails.setText("No information about location");
      }
      else {
         //locationDetails.setVisibility(View.INVISIBLE);
         LatLng point = new LatLng(x, y);
         googleMap.addMarker(new MarkerOptions().position(point).title(""));
         moveToCurrentLocation(point);
      }
   }

   private void moveToCurrentLocation(LatLng currentLocation)
   {
      googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
      // Zoom in, animating the camera.
      googleMap.animateCamera(CameraUpdateFactory.zoomIn());
      // Zoom out to zoom level 10, animating with a duration of 2 seconds.
      googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

   }


}