package com.example.class22bandassignment1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.class22bandassignment1.R;
import com.example.class22bandassignment1.fragments.FragmentMap;

public class Activity_Map extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);

        Fragment fragment = new FragmentMap();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.records_LBL_map,fragment)
                .commit();
    }
}