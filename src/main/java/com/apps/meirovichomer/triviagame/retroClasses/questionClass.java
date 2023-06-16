package com.apps.meirovichomer.triviagame.retroClasses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class questionClass {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer_one")
    @Expose
    private String answerOne;
    @SerializedName("answer_two")
    @Expose
    private String answerTwo;
    @SerializedName("answer_three")
    @Expose
    private String answerThree;
    @SerializedName("correct_answer")
    @Expose
    private String correctAnswer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerOne() {
        return answerOne;
    }

    public void setAnswerOne(String answerOne) {
        this.answerOne = answerOne;
    }

    public String getAnswerTwo() {
        return answerTwo;
    }

    public void setAnswerTwo(String answerTwo) {
        this.answerTwo = answerTwo;
    }

    public String getAnswerThree() {
        return answerThree;
    }

    public void setAnswerThree(String answerThree) {
        this.answerThree = answerThree;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
