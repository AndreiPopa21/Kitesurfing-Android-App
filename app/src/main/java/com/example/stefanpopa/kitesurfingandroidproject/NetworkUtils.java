package com.example.stefanpopa.kitesurfingandroidproject;

import android.util.Log;

import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_user_get_models.Auth_Result;

import retrofit2.Response;

public class NetworkUtils {

    public static void displayResponseGetAllSpot(Response<Spot_All_Result> response){
        int count = response.body().getAll_result_children().size();
        for(int i=0;i<count;i++) {
            Log.d(MainActivity.TAG, "\nonResponse: The item " + i + " has the following details:" +
                    "\nID is: " + response.body().getAll_result_children().get(i).getId() +
                    "\nName is: " + response.body().getAll_result_children().get(i).getName() +
                    "\nCountry is: " + response.body().getAll_result_children().get(i).getCountry() +
                    "\nWhenToGo is: " + response.body().getAll_result_children().get(i).getWhenToGo() +
                    "\nIsFavorite is: " + response.body().getAll_result_children().get(i).getIsFavorite());
        }
    }

    public static void displayResponseUserGet(Response<Auth_Result> response){
        Log.d(MainActivity.TAG,"onResponse:"+
            "\nToken is: "+response.body().getResult_children().getToken()+
            "\nEmail is: "+response.body().getResult_children().getEmail());
    }
}
