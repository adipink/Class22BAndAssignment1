package com.example.class22bandassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_Game extends AppCompatActivity {

    private Thread th;
    private boolean isThreadStopped = false;

    private ImageView[] game_IMG_hearts;
    private MaterialTextView game_LBL_score;
    private MaterialButton game_BTN_up;
    private MaterialButton game_BTN_left;
    private MaterialButton game_BTN_right;
    private MaterialButton game_BTN_down;
    private ImageView[] game_IMG_squares;
    private GameManager gameManager;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameManager = new GameManager();
        findViews();
        createPlayerListener();

    }

    private void changeByTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        checkGame();
                        movePlayerClick();
                        updateScoreByTime();
                        moveVillainTimer();
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private void checkGame(){
        if (gameManager.getCurrentVillainPlace() == gameManager.getCurrentPlayerPlace()) { //villain on player
            clearCharacter();
            gameManager.initPlaces();
            gameManager.reduceLives();
        }

        if (gameManager.isDead()) {
            Toast.makeText(this, "Loser", Toast.LENGTH_LONG).show();
            finishGame();
            return;
        }

    }

    private void finishGame() {finish();}

    private void findViews(){
        game_IMG_hearts = new ImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };
        game_LBL_score = findViewById(R.id.game_LBL_score);
        game_BTN_up = findViewById(R.id.game_BTN_up);
        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTN_right);
        game_BTN_down = findViewById(R.id.game_BTN_down);
        game_BTN_down = findViewById(R.id.game_BTN_down);
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
        };
    }
    /*
    0 - up
    1 - left
    2 - right
    3 - down
     */
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

    private void movePlayerClick(){
        game_IMG_squares[gameManager.getCurrentPlayerPlace()].setImageResource(0);
        gameManager.setCurrentPlayerPlace();
        placeCharacter("player");
    }

    private void moveVillainTimer(){
        int randomDirection = (int) ((Math.random() * (4 - 0)) + 0);
        game_IMG_squares[gameManager.getCurrentVillainPlace()].setImageResource(0);
        gameManager.setVillainPosition(randomDirection);
        placeCharacter("villain");
    }

    private void updateScoreByTime(){
        gameManager.setScore();
        game_LBL_score.setText(String.valueOf(gameManager.getScore()));

        for (int i = 0; i < game_IMG_hearts.length; i++) {
            game_IMG_hearts[i].setVisibility(gameManager.getLives() > i ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void clearCharacter(){
        game_IMG_squares[gameManager.getCurrentPlayerPlace()].setImageResource(0);
        game_IMG_squares[gameManager.getCurrentVillainPlace()].setImageResource(0);
    }



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
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeByTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}