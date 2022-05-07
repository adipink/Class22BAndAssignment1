package com.example.class22bandassignment1.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.class22bandassignment1.callbacks.CallBack_Location;
import com.example.class22bandassignment1.managers.DataManager;
import com.example.class22bandassignment1.utils.MySharedPreference;
import com.example.class22bandassignment1.Player;
import com.example.class22bandassignment1.R;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class Fragment_Score extends Fragment {
    private MaterialButton[] recordsTable;
    private View view;
    private DataManager dm;
    private CallBack_Location callBackLoc;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_score,container,false);
        findViews();
        setRecords();
        return view;
    }

    public void setCallBackList(CallBack_Location callBackLoc) {
        this.callBackLoc = callBackLoc;
    }

    private void setRecords(){
        String json = MySharedPreference.getMe().getString("usersData","");
         dm = new Gson().fromJson(json,DataManager.class);
        dm.sortPlayers();
        showRecords();
    }

    private void showRecords() {
        for(int i = 0; i<dm.getAllPlayers().size(); i++){
            Player oneOfTheBest = dm.getOnePlayer(i);
            recordsTable[i].setVisibility(View.VISIBLE);
            recordsTable[i].setText((i+1)+") "+oneOfTheBest.getName()+"       Score: "+oneOfTheBest.getScore());
            recordsTable[i].setOnClickListener(v ->
                    callBackLoc.setLocation(oneOfTheBest.getLat(),
                            oneOfTheBest.getLongi(),
                            oneOfTheBest.getName()));
        }
    }

    private void findViews() {
        recordsTable = new MaterialButton[]{
               view.findViewById(R.id.score_BTN_winner0),
               view.findViewById(R.id.score_BTN_winner1),
               view.findViewById(R.id.score_BTN_winner2),
               view.findViewById(R.id.score_BTN_winner3),
               view.findViewById(R.id.score_BTN_winner4),
               view.findViewById(R.id.score_BTN_winner5),
               view.findViewById(R.id.score_BTN_winner6),
               view.findViewById(R.id.score_BTN_winner7),
               view.findViewById(R.id.score_BTN_winner8),
               view.findViewById(R.id.score_BTN_winner9),
        };
    }

}
