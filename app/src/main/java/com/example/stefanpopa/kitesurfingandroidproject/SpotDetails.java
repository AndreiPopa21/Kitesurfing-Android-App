package com.example.stefanpopa.kitesurfingandroidproject;

import java.io.Serializable;

public class SpotDetails implements Serializable {

    public String spotId;
    public String name;
    private double longitude;
    private double latitude;
    private int windProbability;
    private String country;
    private String whenToGo;
    private boolean isFavorite;

    public SpotDetails(String spotId,
                       String name,
                       double longitude,
                       double latitude,
                       int windProbability,
                       String country,
                       String whenToGo,
                       boolean isFavorite) {
        this.spotId = spotId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.windProbability = windProbability;
        this.country = country;
        this.whenToGo = whenToGo;
        this.isFavorite = isFavorite;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
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