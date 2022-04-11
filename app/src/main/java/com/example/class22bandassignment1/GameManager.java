package com.example.class22bandassignment1;

public class GameManager {
    private final int ROW = 5;
    private final int COL = 3;
    private  final int PLAYER_BEGIN = 13;
    private final int VILLAIN_BEGIN = 0;
    private int size;
    private int currentPlayerPlace;
    private int playerDirection ;
    private int currentVillainPlace;
    private int villainDirection;
    private int score;
    private int lives;

    public GameManager(){
        initPlaces();
        size = COL*ROW;
        playerDirection = 1;
        villainDirection = 1;
        lives = 3;
        score = 0;

    }
    public void initPlaces(){
        currentPlayerPlace = PLAYER_BEGIN;
        currentVillainPlace = VILLAIN_BEGIN;
    }

    public int getCurrentPlayerPlace() { return currentPlayerPlace; }

    public int getCurrentPlayerDirection(){return playerDirection;}

    public int getCurrentVillainDirection(){return villainDirection;}

    public int getCurrentVillainPlace() { return currentVillainPlace; }

    public void setPlayerDirection(int direction){
        playerDirection = direction;
    }

    public void setCurrentPlayerPlace(){
        currentPlayerPlace = changePositions(playerDirection,currentPlayerPlace);
    }

    public void setVillainPosition(int direction) {
        villainDirection = direction;
        currentVillainPlace = changePositions(villainDirection,currentVillainPlace);
    }

    public void setScore() { this.score++;}

    public void reduceLives() { lives--;}

    public int getLives() { return lives; }

    public boolean isDead(){return lives<=0; }

    public int getScore() { return score; }

    /*
    0  1  2
    3  4  5
    6  7  8
    9  10 11
    12 13 14
    */
    public int changePositions(int direction, int currentPos){
        int newPos;
        switch (direction){
            case 0: //up
                if(currentPos >= 0 && currentPos < COL) // between 0-2 cannot go up
                    newPos = currentPos;
                else
                    newPos = currentPos - COL;
                break;
            case (1): //left
                if(currentPos%COL == 0)// 0,3,6,9,12 cannot go left
                    newPos = currentPos + (COL-1);
                else
                    newPos = currentPos - 1;
                break;
            case (2): //right
                if(currentPos%COL == 2)// 2,5,8,11,14 cannot go right
                    newPos = currentPos - (COL-1);
                else
                    newPos = currentPos +1;
                break;
            case (3): //down
                if(currentPos >= (size-COL) && currentPos < size) // between 12-14 cannot go down
                    newPos = currentPos;
                else
                    newPos = currentPos + COL;
                break;
            default:
                newPos = currentPos;
                break;
        }

        return newPos;
    }
}
