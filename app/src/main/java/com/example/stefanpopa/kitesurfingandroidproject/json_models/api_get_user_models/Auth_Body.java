package com.example.stefanpopa.kitesurfingandroidproject.json_models.api_get_user_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Auth_Body {
    @SerializedName("email")
    @Expose
    private String email;

    public Auth_Body(String email) {
        this.email = email;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
