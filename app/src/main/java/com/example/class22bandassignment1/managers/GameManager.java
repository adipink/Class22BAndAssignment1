package com.example.class22bandassignment1.managers;

import com.example.class22bandassignment1.Player;

import java.util.Random;

public class GameManager {
    private final int ROW = 6;
    private final int COL = 5;
    private final int PLAYER_BEGIN = 27;
    private final int VILLAIN_BEGIN = 0;
    private final int COIN_BEGIN = 17;
    private int size;
    private int prevPlayerPlace;
    private int currentPlayerPlace;
    private int playerDirection ;
    private int prevVillainPlace;
    private int currentVillainPlace;
    private int villainDirection;
    private int coinPlace;
    private int score;
    private int lives;

    private Player player;

    public GameManager(){
        initPlaces();
        size = ROW * COL;
        playerDirection = 1;
        villainDirection = 1;
        lives = 3;
        score = 0;
        this.player = new Player();

    }
    public void initPlaces(){
        prevPlayerPlace = PLAYER_BEGIN;
        prevVillainPlace = VILLAIN_BEGIN;
        currentPlayerPlace = PLAYER_BEGIN;
        currentVillainPlace = VILLAIN_BEGIN;
        coinPlace = COIN_BEGIN;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCoinPlace(){ return coinPlace; }

    public int getCurrentPlayerPlace() { return currentPlayerPlace; }

    public int getCurrentPlayerDirection(){return playerDirection;}

    public int getCurrentVillainDirection(){return villainDirection;}

    public int getCurrentVillainPlace() { return currentVillainPlace; }

    public void setPlayerDirection(int direction){
        playerDirection = direction;
    }

    public void setCurrentPlayerPlace(){
        currentPlayerPlace = changePositions(playerDirection,prevPlayerPlace);
        prevPlayerPlace = currentPlayerPlace;
    }

    public void setVillainPosition(int direction) {
        villainDirection = direction;
        currentVillainPlace = changePositions(villainDirection,prevVillainPlace);
        prevVillainPlace = currentVillainPlace;
    }

    //place coin randomly exclude the current position of villain/player
    public void setCoinPlace(){
        int max = size-1;
        Random rand = new Random();
        int[] exclude = {currentPlayerPlace,currentVillainPlace};
       // int random = rand.nextInt(max - 0 + 1 - 2);
        int random  = (int) (Math.random() * (max - 0));
        for (int ex : exclude) {
            if (random == ex) {
                if(random == max)
                    random--;
                break;
            }
            random++;
        }
        coinPlace = random;
    }


    public void setScore(String action) {
        switch (action){
            case "increase":
                score +=10;
                break;
            case "decrease":
                score -=5;
                break;
            default:
                score++;
                break;
        }
    }

    public void reduceLives() { lives--;}

    public int getLives() { return lives; }

    public boolean isDead(){return lives<=0; }

    public int getScore() { return score; }

    public boolean isCharactersExchangePlaces(){
        if(currentVillainPlace == prevPlayerPlace || currentPlayerPlace == prevVillainPlace)
            return true;
        else
            return false;
    }

    /*
    0  1  2  3  4
    5  6  7  8  9
    10 11 12 13 14
    15 16 17 18 19
    20 21 22 23 24
    25 26 27 28 29
    */
    public int changePositions(int direction, int prevPos){
        int newPos;
        switch (direction){
            case 0: //up
                if(prevPos >= 0 && prevPos < COL) // between 0-4 cannot go up
                    newPos = prevPos;
                else
                    newPos = prevPos - COL;
                break;
            case (1): //left
                if(prevPos%COL == 0)// 0,5,10,15,20,25 cannot go left
                    newPos = prevPos + (COL-1);
                else
                    newPos = prevPos - 1;
                break;
            case (2): //right
                if(prevPos%COL == 4)// 4,9,14,19,24,29 cannot go right
                    newPos = prevPos - (COL-1);
                else
                    newPos = prevPos +1;
                break;
            case (3): //down
                if(prevPos >= (size-COL) && prevPos < size) // between 25-29 cannot go down
                    newPos = prevPos;
                else
                    newPos = prevPos + COL;
                break;
            default:
                newPos = prevPos;
                break;
        }
        return newPos;
    }
}
