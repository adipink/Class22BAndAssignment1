package com.example.class22bandassignment1.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.class22bandassignment1.Player;
import com.example.class22bandassignment1.managers.DataManager;
import com.example.class22bandassignment1.managers.GameManager;
import com.example.class22bandassignment1.managers.MyLocationManager;
import com.example.class22bandassignment1.managers.MySensorManager;
import com.example.class22bandassignment1.R;
import com.example.class22bandassignment1.utils.MySharedPreference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Activity_Game extends AppCompatActivity {
    private String mode;
    private String name;
    //shared objects
    private ImageView[] game_IMG_hearts;
    private MaterialTextView game_LBL_score;
    private ImageView[] game_IMG_squares;
    private GameManager gameManager;
    //media objects
    MediaPlayer hitSong;
    MediaPlayer winCoinSong;
    MediaPlayer loseCoinSong;

    //buttons objects
    private MaterialButton game_BTN_up;
    private MaterialButton game_BTN_left;
    private MaterialButton game_BTN_right;
    private MaterialButton game_BTN_down;
    //sensors objects
    private TextView sensor_LBL_acc;
    private MySensorManager MySensor;
    private SensorManager sensorManager;

    private Timer timerPlayers;
    private TimerTask timerPlayersTask;
    private Timer timerCoin;
    private TimerTask timerCoinTask;
    private Handler handler = new Handler();

    private Bundle bundle;
    private DataManager dataManager;
    private MyLocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create new object
        gameManager = new GameManager();
        locationManager = new MyLocationManager(this);
        dataManager = DataManager.initData();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("PLAYER_NAME");
            gameManager.getPlayer().setName(name);
        } else {
            this.bundle = new Bundle();
        }

        mode = bundle.getString("ACTIVITY_MODE");
        createSounds();


        //open the activity(buttons/sensors) according to the menu's buttons
        if(mode.equals("BUTTONS")) {
            setContentView(R.layout.activity_game_buttons);
        }
        else{
            setContentView(R.layout.activity_game_sensors);
        }
        handleActivity();

        //request location's permission
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            Log.e("ERROR", "Failed to get permission", e.getCause());
        }
    }

    private void handleActivity(){
        findViews();
        if(mode.equals("BUTTONS")){
            createPlayerListener();
        }else{
            MySensor = new MySensorManager();
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            MySensor.setSensorManager(sensorManager);
            MySensor.initSensor();
        }
    }

    private void createSounds() {
        hitSong = MediaPlayer.create(Activity_Game.this,R.raw.bear_hit);
        winCoinSong = MediaPlayer.create(Activity_Game.this,R.raw.fish_win);
        loseCoinSong = MediaPlayer.create(Activity_Game.this,R.raw.fish_lose);
    }


    /*
    call it on OnResume
    call to update the score , move each character and check game's status
     */
    private void changeByTimer() {
        timerCoin = new Timer();
        timerCoinTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        moveCoin();
                    }
                });
            }
        };
        timerCoin.schedule(timerCoinTask,0,5000);
        timerPlayers = new Timer();
        timerPlayersTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        checkGame();
                        movePlayer();
                        updateScoreByTime();
                        moveVillainTimer();
                    }
                });
            }
        };
        timerPlayers.schedule(timerPlayersTask, 0, 1000);
    }

    private void moveCoin() {
        game_IMG_squares[gameManager.getCoinPlace()].setImageResource(0);
        gameManager.setCoinPlace();
        placeCharacter("coin");
    }

    //update score +1 each second
    private void updateScoreByTime(){
        gameManager.setScore("");
        game_LBL_score.setText(String.valueOf(gameManager.getScore()));

        for (int i = 0; i < game_IMG_hearts.length; i++) {
            game_IMG_hearts[i].setVisibility(gameManager.getLives() > i ? View.VISIBLE : View.INVISIBLE);
        }
    }

    //check if hunter got bear && if bear is dead and game is over
    private void checkGame(){
        if (gameManager.getCurrentVillainPlace() == gameManager.getCurrentPlayerPlace()) { //villain on player
            playHitPlayerSong();
            vibrateByAction();
            clearCharacter();
            gameManager.initPlaces();
            gameManager.reduceLives();
        }
        if(gameManager.getCurrentPlayerPlace() == gameManager.getCoinPlace()){ //player got coin
            playWinCoinSong();
            vibrateByAction();
            gameManager.setScore("increase");
        }
        if(gameManager.getCurrentVillainPlace() == gameManager.getCoinPlace()){ //villain got coin
            playLoseCoinSong();
            vibrateByAction();
            gameManager.setScore("decrease");
        }

        if (gameManager.isDead()) {
            Toast.makeText(this, "Game Over! Goodbye", Toast.LENGTH_LONG).show();
            finishGame();
            finish();
            return;
        }
    }

    private void playHitPlayerSong(){
        hitSong.start();
    }

    private void playWinCoinSong(){
        winCoinSong.start();
    }
    private void playLoseCoinSong(){
        loseCoinSong.start();
    }

    private void vibrateByAction() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = {100, 100, 100, 200};
        int[] strength = {1, 50, 1, 50};


        if (Build.VERSION.SDK_INT >= 26) {
            VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, strength, -1);
            v.vibrate(vibrationEffect);
        } else {
            v.vibrate(pattern, -1);
        }
    }


    private void finishGame() {
        Player player = new Player()
                .setName(gameManager.getPlayer().getName())
                .setScore(gameManager.getScore())
                .setLongi(locationManager.getLongi())
                .setLat(locationManager.getLat());

        dataManager.addPlayer(player);
        String json = new Gson().toJson(dataManager);
        MySharedPreference.getMe().putString("usersData",json);
        Intent intent = new Intent(this, Activity_Records.class);
        bundle.putString("PLAYER_NAME",name);
        bundle.putString("ACTIVITY_MODE",mode);
        intent.putExtras(bundle);
        startActivity(intent);
        }



    /*
    0 - up
    1 - left
    2 - right
    3 - down
     */
    //create listener to buttons
    private void createPlayerListener(){
        game_BTN_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.setPlayerDirection(0);
            }
        });

        game_BTN_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.setPlayerDirection(1);
            }
        });

        game_BTN_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.setPlayerDirection(2);
            }
        });

        game_BTN_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.setPlayerDirection(3);
            }
        });
    }


    /*
    0 - up
    1 - left
    2 - right
    3 - down
     */
    private SensorEventListener accSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            changedSensor(sensorEvent);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            // empty
        }
    };

    private void changedSensor(SensorEvent sensorEvent){
        try {
            NumberFormat formatter = new DecimalFormat("#0.00");
            float x = sensorEvent.values[0] ;
            float y = sensorEvent.values[1] ;
            float z = sensorEvent.values[2] ;

            /*
            UP == y<3.5
            DOWN == y>3.5
            RIGHT == x < -0.5
            LEFT ==  x> 0.5
             */
            //TODO: check directions

            if (x < -1.2) {// move right
                gameManager.setPlayerDirection(2);
            }else if (x > 1.2) {// move left
                gameManager.setPlayerDirection(1);
            }
            if(y < 2.9) {// move up
                gameManager.setPlayerDirection(0);
            }else if(y > 5.2) {// move down
                gameManager.setPlayerDirection(3);
            }


           // sensor_LBL_acc.setText("x = "+ formatter.format(x) + "\n" + "y = "+formatter.format(y)+"\n" + "z = "+formatter.format(z));
        } catch (Exception e) {
            Log.e("ERROR", "Failed to get sensor values", e.getCause());
        }
    }

    //change player direction by pressing buttons
    private void movePlayer(){
        if(!gameManager.isCharactersExchangePlaces())
            game_IMG_squares[gameManager.getCurrentPlayerPlace()].setImageResource(0);
        gameManager.setCurrentPlayerPlace();
        placeCharacter("player");
    }

    //change villain direction each second
    private void moveVillainTimer(){
        int randomDirection = (int) (Math.random() * (4 - 0));
        if(!gameManager.isCharactersExchangePlaces())
            game_IMG_squares[gameManager.getCurrentVillainPlace()].setImageResource(0);
        gameManager.setVillainPosition(randomDirection);
        placeCharacter("villain");
    }

    //clear the character on board from previous place
    public void clearCharacter(){
        game_IMG_squares[gameManager.getCurrentPlayerPlace()].setImageResource(0);
        game_IMG_squares[gameManager.getCurrentVillainPlace()].setImageResource(0);
        game_IMG_squares[gameManager.getCoinPlace()].setImageResource(0);
    }


    //place the character on board to new place
    private void placeCharacter(String Character){
        if(Character.equals("player")){
            String PlayerName = "ic_polarbear" + gameManager.getCurrentPlayerDirection();
            int playerId = this.getResources().getIdentifier(PlayerName,"drawable",this.getPackageName());
            game_IMG_squares[gameManager.getCurrentPlayerPlace()].setImageResource(playerId);
        }
        if(Character.equals("villain")){
            String VillainName = "ic_hunter" + gameManager.getCurrentVillainDirection();
            int villainId = this.getResources().getIdentifier(VillainName,"drawable",this.getPackageName());
            game_IMG_squares[gameManager.getCurrentVillainPlace()].setImageResource(villainId);
        }
        if(Character.equals("coin")){
            String CoinName = "ic_fishcoin" ;
            int coinId = this.getResources().getIdentifier(CoinName,"drawable",this.getPackageName());
            game_IMG_squares[gameManager.getCoinPlace()].setImageResource(coinId);
        }
    }


    //activate the timer actions
    @Override
    protected void onResume() {
        super.onResume();
        changeByTimer();
        if(mode.equals("SENSORS")){
            MySensor.getSensorManager().registerListener(accSensorEventListener, MySensor.getAccSensor(), SensorManager.SENSOR_DELAY_NORMAL);
        }


    }

    //stop the timer
    @Override
    protected void onPause() {
        super.onPause();
        timerPlayers.cancel();
        timerCoin.cancel();
        if(mode.equals("SENSORS")){
            MySensor.getSensorManager().unregisterListener(accSensorEventListener);
        }

    }

    //find all the views of the program
    private void findViews(){
        game_IMG_hearts = new ImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };
        game_LBL_score = findViewById(R.id.game_LBL_score);
        game_IMG_squares = new ImageView[]{
                findViewById(R.id.game_IMG_square0),
                findViewById(R.id.game_IMG_square1),
                findViewById(R.id.game_IMG_square2),
                findViewById(R.id.game_IMG_square3),
                findViewById(R.id.game_IMG_square4),
                findViewById(R.id.game_IMG_square5),
                findViewById(R.id.game_IMG_square6),
                findViewById(R.id.game_IMG_square7),
                findViewById(R.id.game_IMG_square8),
                findViewById(R.id.game_IMG_square9),
                findViewById(R.id.game_IMG_square10),
                findViewById(R.id.game_IMG_square11),
                findViewById(R.id.game_IMG_square12),
                findViewById(R.id.game_IMG_square13),
                findViewById(R.id.game_IMG_square14),
                findViewById(R.id.game_IMG_square15),
                findViewById(R.id.game_IMG_square16),
                findViewById(R.id.game_IMG_square17),
                findViewById(R.id.game_IMG_square18),
                findViewById(R.id.game_IMG_square19),
                findViewById(R.id.game_IMG_square20),
                findViewById(R.id.game_IMG_square21),
                findViewById(R.id.game_IMG_square22),
                findViewById(R.id.game_IMG_square23),
                findViewById(R.id.game_IMG_square24),
                findViewById(R.id.game_IMG_square25),
                findViewById(R.id.game_IMG_square26),
                findViewById(R.id.game_IMG_square27),
                findViewById(R.id.game_IMG_square28),
                findViewById(R.id.game_IMG_square29),
        };
        if(mode.equals("BUTTONS")){
            game_BTN_up = findViewById(R.id.game_BTN_up);
            game_BTN_left = findViewById(R.id.game_BTN_left);
            game_BTN_right = findViewById(R.id.game_BTN_right);
            game_BTN_down = findViewById(R.id.game_BTN_down);
        }else{
            sensor_LBL_acc = findViewById(R.id.sensor_LBL_acc);
        }

    }


}