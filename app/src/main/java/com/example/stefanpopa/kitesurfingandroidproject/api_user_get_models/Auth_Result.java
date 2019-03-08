package com.example.stefanpopa.kitesurfingandroidproject.api_user_get_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Auth_Result {

    @SerializedName("result")
    @Expose
    private Auth_Result_Children result_children;

    public Auth_Result_Children getResult_children() {
        return result_children;
    }

    public void setResult_children(Auth_Result_Children result_children) {
        this.result_children = result_children;
    }
}
