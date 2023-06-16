package com.apps.meirovichomer.triviagame.retroClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TopSessionClass {

    @SerializedName("Sessions")
    @Expose
    private ArrayList<topSessionsClass> sessions = null;

    public ArrayList<topSessionsClass> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<topSessionsClass> scores) {
        this.sessions = scores;
    }
}
