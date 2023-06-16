package com.apps.meirovichomer.triviagame.retroClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class sessionInfoClass {

    @SerializedName("score")
    @Expose
    private String score;

    @SerializedName("level")
    @Expose
    private String levelId;

    @SerializedName("session_time")
    @Expose
    private String sessionTime;


    public String getScore(){
        return score;
    }
    public void setScore(String score){
        this.score = score;
    }
    public String getLevel(){
        return levelId;
    }
    public void setLevelId(String levelId){
        this.levelId=levelId;
    }
    public String getSessionTime(){
        return sessionTime;
    }
    public void setSessionTime(String sessionTime){
        this.sessionTime = sessionTime;
    }

}
