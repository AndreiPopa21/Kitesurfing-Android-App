package com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_countries_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Spot_Countries_Result {

    @SerializedName("result")
    @Expose
    private ArrayList<String> countries;

    public ArrayList<String> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<String> countries) {
        this.countries = countries;
    }

}
