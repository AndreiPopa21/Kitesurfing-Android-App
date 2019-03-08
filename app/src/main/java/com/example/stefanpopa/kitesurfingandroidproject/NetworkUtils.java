package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.res.Resources;
import android.util.Log;

import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_details_models.Spot_Details_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_details_models.Spot_Details_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_user_get_models.Auth_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_user_get_models.Auth_Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    public static void displayResponseGetAllSpot(Response<Spot_All_Result> response){
        int count = response.body().getAll_result_children().size();
        for(int i=0;i<count;i++) {
            Log.d(MainActivity.TAG, "\nonResponse: The item " + i + " has the following details:" +
                    "\nID is: " + response.body().getAll_result_children().get(i).getId() +
                    "\nName is: " + response.body().getAll_result_children().get(i).getName() +
                    "\nCountry is: " + response.body().getAll_result_children().get(i).getCountry() +
                    "\nWhenToGo is: " + response.body().getAll_result_children().get(i).getWhenToGo() +
                    "\nIsFavorite is: " + response.body().getAll_result_children().get(i).isFavorite());
        }
    }

    public static void displayResponseGetSpotDetails(Response<Spot_Details_Result> response){
        Log.d(MainActivity.TAG,"The response values are following: "+
            "\nID is: "+response.body().getDetails_result_children().getId()+
            "\nName is: "+response.body().getDetails_result_children().getName()+
            "\nLongitude is: "+response.body().getDetails_result_children().getLongitude()+
            "\nLatitude is: "+response.body().getDetails_result_children().getLatitude()+
            "\nWindProbability is: "+response.body().getDetails_result_children().getWindProbability()+
            "\nCountry is: "+response.body().getDetails_result_children().getCountry()+
            "\nWhenToGo is: "+response.body().getDetails_result_children().getWhenToGo()+
            "\nIsFavorite is: "+response.body().getDetails_result_children().isFavorite());
    }

    public static void displayResponseUserGet(Response<Auth_Result> response){
        Log.d(MainActivity.TAG,"onResponse:"+
            "\nToken is: "+response.body().getResult_children().getToken()+
            "\nEmail is: "+response.body().getResult_children().getEmail());
    }

    public static void sendNetworkSpotDetailsRequest(Spot_Details_Body body,String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KitesurfingAPI kitesurfingAPI = retrofit.create(KitesurfingAPI.class);

        Call<Spot_Details_Result> call = kitesurfingAPI.getSpotDetails(body);

        call.enqueue(new Callback<Spot_Details_Result>() {
            @Override
            public void onResponse(Call<Spot_Details_Result> call, Response<Spot_Details_Result> response) {
                Log.d(MainActivity.TAG,"onResponse: The call on api-spot-get-details endpoint has been succesful");
                NetworkUtils.displayResponseGetSpotDetails(response);
            }

            @Override
            public void onFailure(Call<Spot_Details_Result> call, Throwable t) {
                Log.d(MainActivity.TAG,"onFailure: Something went wrong with the server");
                Log.d(MainActivity.TAG,t.toString());
            }
        });
    }

    public static void sendNetworkSpotAllRequest(Spot_All_Body body,String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KitesurfingAPI kitesurfingAPI = retrofit.create(KitesurfingAPI.class);

        Call<Spot_All_Result> call = kitesurfingAPI.getSpotAll(body);

        call.enqueue(new Callback<Spot_All_Result>() {
            @Override
            public void onResponse(Call<Spot_All_Result> call, Response<Spot_All_Result> response) {
                NetworkUtils.displayResponseGetAllSpot(response);
            }

            @Override
            public void onFailure(Call<Spot_All_Result> call, Throwable t) {
                Log.d(MainActivity.TAG,"onFailure: Something went wrong with the server");
                Log.d(MainActivity.TAG,t.toString());
            }
        });
    }

    public static void sendNetworkGetUserRequest(Auth_Body body, String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KitesurfingAPI kitesurfingAPI = retrofit.create(KitesurfingAPI.class);

        Call<Auth_Result> call = kitesurfingAPI.getAuth(body);

        call.enqueue(new Callback<Auth_Result>() {
            @Override
            public void onResponse(Call<Auth_Result> call, Response<Auth_Result> response) {
                Log.d(MainActivity.TAG,"onResponse: The call on the api-user-get endpoint has been succesful");
                NetworkUtils.displayResponseUserGet(response);
            }

            @Override
            public void onFailure(Call<Auth_Result> call, Throwable t) {
                Log.d(MainActivity.TAG,"onFailure: Something went wrong with the server");
                Log.d(MainActivity.TAG,t.toString());
            }
        });
    }
}
