package com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Spot_All_Result implements Serializable {

    @SerializedName("result")
    @Expose
    private ArrayList<Spot_All_Result_Children> all_result_children;

    public ArrayList<Spot_All_Result_Children> getAll_result_children() {
        return all_result_children;
    }

    public void setAll_result_children(ArrayList<Spot_All_Result_Children> all_result_children) {
        this.all_result_children = all_result_children;
    }
}
