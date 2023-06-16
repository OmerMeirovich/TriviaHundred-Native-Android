package com.apps.meirovichomer.triviagame.retroClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meiro on 10/24/2017.
 */

public class setNewNameClass {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("output")
    @Expose
    private String output;


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
