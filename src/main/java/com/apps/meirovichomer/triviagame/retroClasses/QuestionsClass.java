package com.apps.meirovichomer.triviagame.retroClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class QuestionsClass {

    @SerializedName("Questions")
    @Expose
    private ArrayList<questionClass> questions = null;

    public ArrayList<questionClass> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<questionClass> questions) {
        this.questions = questions;
    }


}
