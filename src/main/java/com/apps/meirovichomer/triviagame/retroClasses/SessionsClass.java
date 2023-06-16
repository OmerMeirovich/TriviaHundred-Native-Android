package com.apps.meirovichomer.triviagame.retroClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SessionsClass {

    @SerializedName("Sessions")
    @Expose
    private ArrayList<sessionInfoClass> sessions = null;

    public ArrayList<sessionInfoClass> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<sessionInfoClass> questions) {
        this.sessions = questions;
    }
}
