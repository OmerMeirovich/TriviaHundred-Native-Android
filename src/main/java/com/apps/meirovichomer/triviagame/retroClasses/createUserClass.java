package com.apps.meirovichomer.triviagame.retroClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meiro on 10/7/2017.
 */

public class createUserClass {

    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("output")
    @Expose
    private String output;


    public String getRandomId() {
        return id;
    }

    public void setRandomId(String random_id) {

        this.id = random_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {

        this.output = output;
    }


}
