package com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_details_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Spot_Details_Body {

    @SerializedName("spotId")
    @Expose
    private String spotId;

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }
}
