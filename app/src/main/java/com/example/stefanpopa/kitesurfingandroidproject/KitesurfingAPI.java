package com.example.stefanpopa.kitesurfingandroidproject;

import com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_add_models.Favorites_Add_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_add_models.Favorites_Add_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_remove_models.Favorites_Remove_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_remove_models.Favorites_Remove_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_countries_models.Spot_Countries_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_details_models.Spot_Details_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_details_models.Spot_Details_Result;
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

    @Headers({"Content-Type: application/json",
            "token: StdwDgMrAn"})
    @POST("/api-spot-get-details")
    Call<Spot_Details_Result> getSpotDetails(@Body Spot_Details_Body details);

    @Headers({"Content-Type: application/json",
            "token: StdwDgMrAn"})
    @POST("/api-spot-get-countries")
    Call<Spot_Countries_Result> getSpotCountries();

    @Headers({"Content-Type: application/json",
            "token: StdwDgMrAn"})
    @POST("/api-spot-favorites-add")
    Call<Favorites_Add_Result> getFavoritesAdd(@Body Favorites_Add_Body body);

    @Headers({"Content-Type: application/json",
            "token: StdwDgMrAn"})
    @POST("/api-spot-favorites-remove")
    Call<Favorites_Remove_Result> getFavoritesAdd(@Body Favorites_Remove_Body body);

}
