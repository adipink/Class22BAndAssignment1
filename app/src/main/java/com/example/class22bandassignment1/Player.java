package com.example.class22bandassignment1;


import android.util.Log;

import com.google.gson.Gson;

public class Player implements Comparable<Player>{
    private String name;
    private int score;
    private double lat;
    private double longi;

    public Player(){
        score = 0;
        lat = 0.0;
        longi = 0.0;
        name = "";
    }

        public int getScore() {
            return score;
        }

        public Player setScore(int score) {
            this.score = score;
            return this;
        }

        public double getLat() {
            return lat;
        }

        public Player setLat(double lat) {
            this.lat = lat;
            return this;
        }

        public double getLongi() {
            return longi;
        }

        public Player setLongi(double lon) {
            this.longi = lon;
            return this;
        }

        public String getName() {
            return name;
        }

        public Player setName(String name) {
            this.name = name;
            return this;
        }


    @Override
    public int compareTo(Player player) {
        if (player.score > this.score){
            return 1;
        }else if(player.score < this.score){
            return -1;
        }
        return  0;
    }
}