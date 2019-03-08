package com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Spot_All_Body {

    @SerializedName("country")
    @Expose
    private String country;

    public Spot_All_Body(String country, int windProbability) {
        this.country = country;
        this.windProbability = windProbability;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setWindProbability(int windProbability) {
        this.windProbability = windProbability;
    }

    @SerializedName("windProbability")
    @Expose
    private int windProbability;
}
