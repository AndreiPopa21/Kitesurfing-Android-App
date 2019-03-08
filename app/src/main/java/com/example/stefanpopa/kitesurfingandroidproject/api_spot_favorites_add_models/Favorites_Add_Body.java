package com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_add_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favorites_Add_Body {
    @SerializedName("spotId")
    @Expose
    private String spotId;

    public Favorites_Add_Body(String spotId) {
        this.spotId = spotId;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }
}
