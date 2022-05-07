package com.example.class22bandassignment1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.class22bandassignment1.utils.MySharedPreference;
import com.example.class22bandassignment1.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Activity_OpeningScreen extends AppCompatActivity{

    private MaterialButton open_BTN_exit;
    private MaterialTextView open_LBL_title;
    private MaterialButton open_BTN_startPlayer;
    private MaterialButton open_BTN_enterName;
    private EditText open_EDT_playerName;

    private MaterialButton open_BTN_startButtons;
    private MaterialButton open_BTN_startSensors;
    private MaterialButton open_BTN_topTen;

    private Bundle bundle = null;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        MySharedPreference.init(this);
        if (bundle == null){
            bundle = new Bundle();
        }
        findViews();
        createButtonsListener();
        newScreen();

    }
    private  void newScreen(){
        open_BTN_startPlayer.setVisibility(View.VISIBLE);
        open_BTN_enterName.setVisibility(View.INVISIBLE);
        open_EDT_playerName.setVisibility(View.INVISIBLE);
        open_BTN_startButtons.setVisibility(View.INVISIBLE);
        open_BTN_startSensors.setVisibility(View.INVISIBLE);
        open_BTN_topTen.setVisibility(View.INVISIBLE);
    }
    private void loginScreen(){
        open_BTN_startPlayer.setVisibility(View.INVISIBLE);
        open_BTN_enterName.setVisibility(View.VISIBLE);
        open_EDT_playerName.setVisibility(View.VISIBLE);
        open_BTN_startButtons.setVisibility(View.INVISIBLE);
        open_BTN_startSensors.setVisibility(View.INVISIBLE);
        open_BTN_topTen.setVisibility(View.INVISIBLE);
    }

    private void modeScreen(){
        open_BTN_enterName.setVisibility(View.INVISIBLE);
        open_EDT_playerName.setVisibility(View.INVISIBLE);
        open_BTN_startButtons.setVisibility(View.VISIBLE);
        open_BTN_startSensors.setVisibility(View.VISIBLE);
        open_BTN_topTen.setVisibility(View.VISIBLE);
    }

    private void findViews() {
        open_BTN_exit = findViewById(R.id.open_BTN_exit);
        open_LBL_title = findViewById(R.id.open_LBL_title);
        open_BTN_startButtons = findViewById(R.id.open_BTN_startButtons);
        open_BTN_startSensors = findViewById(R.id.open_BTN_startSensors);
        open_BTN_topTen = findViewById(R.id.open_BTN_topTen);


        open_BTN_startPlayer = findViewById(R.id.open_BTN_startPlayer);
        open_BTN_enterName = findViewById(R.id.open_BTN_enterName);
        open_EDT_playerName = findViewById(R.id.open_EDT_playerName);
    }

    private void createButtonsListener(){
        open_BTN_startPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginScreen();
            }
        });
        open_BTN_enterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = open_EDT_playerName.getText().toString();
                if(name.isEmpty()){
                    name= "anonymous";
                }
                modeScreen();
            }
        });

        open_BTN_startButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Game.class);
                bundle.putString("PLAYER_NAME",name);
                bundle.putString("ACTIVITY_MODE","BUTTONS");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        open_BTN_startSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Game.class);
                bundle.putString("PLAYER_NAME",name);
                bundle.putString("ACTIVITY_MODE","SENSORS");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        open_BTN_topTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Records.class);
                startActivity(intent);
                finish();
            }
        });

        open_BTN_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Game is over", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

}
