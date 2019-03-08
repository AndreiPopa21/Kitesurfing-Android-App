package com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Spot_All_Result_Children {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("whenToGo")
    @Expose
    private String whenToGo;

    @SerializedName("isFavorite")
    @Expose
    private String isFavorite;

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

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }
}
