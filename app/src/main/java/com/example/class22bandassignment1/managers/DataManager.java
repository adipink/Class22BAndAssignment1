package com.example.class22bandassignment1.managers;

import com.example.class22bandassignment1.Player;

import java.util.ArrayList;
import java.util.Collections;

public class DataManager {
    private ArrayList<Player> allPlayers;
    private static DataManager dataManager;

    //init array list of players
    public DataManager(){
        allPlayers = new ArrayList<Player>();
    }

    public static DataManager getDataManager(){
        return dataManager;
    }
    //init data manager
    public static DataManager initData(){
        if (dataManager == null) {
            dataManager = new DataManager();
        }
        return dataManager;
    }


    //add player to list
    public void addPlayer(Player player){
        allPlayers.add(player);

       /* boolean isUpdated = false;
        for(Player p: allPlayers){
            if(p.getName().equals(player.getName())){
                if(p.getScore()<player.getScore()) {
                    p.setScore(player.getScore());
                }
                isUpdated = true;
            }
        }
        if(!isUpdated){
            allPlayers.add(player);
        }
*/
    }

    //return list of players
    public ArrayList<Player> getAllPlayers(){
        return allPlayers;
    }

    public Player getOnePlayer(int place){
        return allPlayers.get(place);
    }

    //sort players by score
    public void sortPlayers(){
        Collections.sort(allPlayers);
    }

}
