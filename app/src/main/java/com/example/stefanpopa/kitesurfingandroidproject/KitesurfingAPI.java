package com.example.stefanpopa.kitesurfingandroidproject;

import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_user_get_models.Auth_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_user_get_models.Auth_Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface KitesurfingAPI {


    @Headers("Content-Type: application/json")
    @POST("/api-user-get")
    Call<Auth_Result> getAuth(@Body Auth_Body auth);

    @Headers({"Content-Type: application/json",
              "token: StdwDgMrAn"})
    @POST("/api-spot-get-all")
    Call<Spot_All_Result> getSpotAll(@Body Spot_All_Body spot);
}
