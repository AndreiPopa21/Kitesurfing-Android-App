package com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_details_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Spot_Details_Result_Children {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("longitude")
    @Expose
    private int longitude;
    @SerializedName("latitude")
    @Expose
    private int latitude;
    @SerializedName("windProbability")
    @Expose
    private int windProbability;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("whenToGo")
    @Expose
    private String whenToGo;
    @SerializedName("isFavorite")
    @Expose
    private boolean isFavorite;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getWindProbability() {
        return windProbability;
    }

    public void setWindProbability(int windProbability) {
        this.windProbability = windProbability;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWhenToGo() {
        return whenToGo;
    }

    public void setWhenToGo(String whenToGo) {
        this.whenToGo = whenToGo;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
