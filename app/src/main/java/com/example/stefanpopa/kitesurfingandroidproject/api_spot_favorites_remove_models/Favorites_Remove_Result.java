package com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_remove_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favorites_Remove_Result {

    @SerializedName("result")
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
