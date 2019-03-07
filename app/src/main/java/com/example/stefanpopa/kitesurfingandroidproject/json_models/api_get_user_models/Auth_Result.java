package com.example.stefanpopa.kitesurfingandroidproject.json_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Auth_Result {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("email")
    @Expose
    private String email;

    public Auth_Result(String email) {
        this.email = email;
    }

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
