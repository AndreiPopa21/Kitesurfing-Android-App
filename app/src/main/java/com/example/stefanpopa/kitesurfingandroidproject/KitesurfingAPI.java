package com.example.stefanpopa.kitesurfingandroidproject;

import com.example.stefanpopa.kitesurfingandroidproject.json_models.api_get_user_models.Auth_Body;
import com.example.stefanpopa.kitesurfingandroidproject.json_models.api_get_user_models.Auth_Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface KitesurfingAPI {


    @Headers("Content-Type: application/json")
    @POST("/api-user-get")
    Call<Auth_Result> getAuth(@Body Auth_Body auth);
}
