package com.apps.meirovichomer.triviagame.retroClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class topSessionsClass {


    @SerializedName("score")
    @Expose
    String score;


    @SerializedName("user_name")
    @Expose
    String userName;


    public String getScore() {
        return score;
    }

    public String getPlayer_name() {
        return userName;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setPlayer_name(String player_name) {
        this.userName = player_name;
    }


}
