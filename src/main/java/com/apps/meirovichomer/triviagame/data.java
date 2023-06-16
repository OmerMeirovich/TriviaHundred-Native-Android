package com.apps.meirovichomer.triviagame;

import java.util.HashMap;
import java.util.Map;

/**
 * data class will hold a map, used to send a request to the server via Retrofit @QueryMap.
 */

public class data {

    private Map<String, String> data = new HashMap<>();

    public void putParam(String key, String value) {
        data.put(key, value);
    }

    public Map<String, String> getParamsData(String action){

        data.put("action",action);

        return data;
    }


}
