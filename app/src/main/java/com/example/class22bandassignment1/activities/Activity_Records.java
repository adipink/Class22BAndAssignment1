package com.example.class22bandassignment1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.class22bandassignment1.callbacks.CallBack_Location;
import com.example.class22bandassignment1.fragments.FragmentMap;
import com.example.class22bandassignment1.fragments.Fragment_Score;
import com.example.class22bandassignment1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Activity_Records extends AppCompatActivity {

    private Fragment_Score fragmentScore;
    private FragmentMap fragmentMap;
    private Bundle bundle;
    private MaterialButton records_BTN_back;
    private MaterialButton records_BTN_playAgain;
    private MaterialTextView records_LBL_topTen;
    private MaterialTextView records_LBL_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        bundle = getIntent().getExtras();
        findViews();

        //Initialize MAP fragment
        fragmentMap = new FragmentMap();
        //open fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.records_LAY_map,fragmentMap)
                .commit();

        //Initialize SCORE fragment
        fragmentScore = new Fragment_Score();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.records_LAY_list,fragmentScore)
                .commit();

        fragmentScore.setCallBackList((lat, lon, playerName) -> fragmentMap.locateOnMap(lat,lon));

        createListener();


    }

    private void createListener() {
        records_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_OpeningScreen.class);
                bundle.putString("PLAYER_NAME","");
                bundle.putString("ACTIVITY_MODE",bundle.getString("ACTIVITY_MODE"));
                startActivity(intent);
                finish();
            }
        });
        records_BTN_playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Game.class);
                bundle.putString("PLAYER_NAME","");
                bundle.putString("ACTIVITY_MODE",bundle.getString("ACTIVITY_MODE"));
                startActivity(intent);
                finish();
            }
        });
    }

    private void findViews() {
        records_BTN_back = findViewById(R.id.records_BTN_back);
        records_BTN_playAgain = findViewById(R.id.records_BTN_playAgain);
        records_LBL_topTen = findViewById(R.id.records_LBL_topTen);
        records_LBL_map = findViewById(R.id.records_LBL_map);

    }

    private void zoomOnMap(double lat, double lon) {
        GoogleMap gm = fragmentMap.getGoogleMap();
        LatLng point = new LatLng(lat, lon);
        gm.addMarker(new MarkerOptions().position(point).title("player"));
        gm.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13.0f));
    }

    CallBack_Location callBackLocation = new CallBack_Location() {
        @Override
        public void setLocation(double lat, double longi, String name) {
            zoomOnMap(lat, longi);
        }
    };

}