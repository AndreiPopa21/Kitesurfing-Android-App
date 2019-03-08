package com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_details_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Spot_Details_Result {

    @SerializedName("result")
    @Expose
    private Spot_Details_Result_Children details_result_children;

    public Spot_Details_Result_Children getDetails_result_children() {
        return details_result_children;
    }

    public void setDetails_result_children(Spot_Details_Result_Children details_result_children) {
        this.details_result_children = details_result_children;
    }
}
