package com.example.stefanpopa.kitesurfingandroidproject.api_user_get_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Auth_Result_Children {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("email")
    @Expose
    private String email;

    @Override
    public String toString() {
        return "Auth_Result{" +
                "token='" + token + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
