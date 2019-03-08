package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

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
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    public static void displayResponseGetAllSpot(Response<Spot_All_Result> response){
        switch(response.code()){
            case 200:
                int count = response.body().getAll_result_children().size();
                for(int i=0;i<count;i++) {
                    Log.d(MainActivity.TAG, "\nonResponse: The item " + i + " has the following details:" +
                            "\nID is: " + response.body().getAll_result_children().get(i).getId() +
                            "\nName is: " + response.body().getAll_result_children().get(i).getName() +
                            "\nCountry is: " + response.body().getAll_result_children().get(i).getCountry() +
                            "\nWhenToGo is: " + response.body().getAll_result_children().get(i).getWhenToGo() +
                            "\nIsFavorite is: " + response.body().getAll_result_children().get(i).isFavorite());
                }
                break;
            default:
                Log.d(MainActivity.TAG,"Result could not be displayed due to: "+response.code()+" code");
        }

    }

    public static void displayResponseGetSpotDetails(Response<Spot_Details_Result> response){
        switch(response.code()) {
            case 200:
                Log.d(MainActivity.TAG, "The response values are following: " +
                        "\nID is: " + response.body().getDetails_result_children().getId() +
                        "\nName is: " + response.body().getDetails_result_children().getName() +
                        "\nLongitude is: " + response.body().getDetails_result_children().getLongitude() +
                        "\nLatitude is: " + response.body().getDetails_result_children().getLatitude() +
                        "\nWindProbability is: " + response.body().getDetails_result_children().getWindProbability() +
                        "\nCountry is: " + response.body().getDetails_result_children().getCountry() +
                        "\nWhenToGo is: " + response.body().getDetails_result_children().getWhenToGo() +
                        "\nIsFavorite is: " + response.body().getDetails_result_children().isFavorite());
                break;
            default:
                Log.d(MainActivity.TAG, "Result could not be displayed due to " + response.code()+" code");
        }
    }

    public static void displayResponseUserGet(Response<Auth_Result> response){
        switch(response.code()) {
            case 200:
                Log.d(MainActivity.TAG,"onResponse:"+
                        "\nToken is: "+response.body().getResult_children().getToken()+
                        "\nEmail is: "+response.body().getResult_children().getEmail());
                break;
            default:
                Log.d(MainActivity.TAG, "Result could not be displayed due to " + response.code() + " code");
        }
    }

    public static void displayResponseGetSpotCountries(Response<Spot_Countries_Result> response){
        switch(response.code()){
            case 200:
                int count = response.body().getCountries().size();
                for(int i=0;i<count;i++){
                    Log.d(MainActivity.TAG,response.body().getCountries().get(i));
                }
                break;
            default:
                Log.d(MainActivity.TAG,"Result could not be displayed due to "+response.code()+" code");
        }
    }

    public static void displayResponseAddFavorites(Response<Favorites_Add_Result> response){

        switch(response.code()){
            case 200:
                Log.d(MainActivity.TAG,"Result is: "+response.body().getResult());
                break;
            case 500:
                Log.d(MainActivity.TAG,"Result could not be displayed due to 500 code");
                //Log.d(MainActivity.TAG,"SASASASASA");
                //Log.d(MainActivity.TAG,"EEEE: "+response.errorBody().toString());
                /*Log.d(MainActivity.TAG, "Error is: "+response.body().getError().getMessage()+
                        " / "+response.body().getError().getCode()+
                        "\nSentBodyParameters: "+response.body().getSentBodyParameters().getSpotId()+
                        "\nSentHeaders: "+response.body().getSentHeaders().getContent_type()+
                        " / "+response.body().getSentHeaders().getToken());*/
                break;
        }
    }

    public static void displayResponseRemoveFavorites(Response<Favorites_Remove_Result> response){

        switch(response.code()){
            case 200:
                Log.d(MainActivity.TAG,"Result is: "+response.body().getResult());
                break;
            case 500:
                Log.d(MainActivity.TAG,"Result could not be displayed due to 500 code");
                //Log.d(MainActivity.TAG,"SASASASASA");
                //Log.d(MainActivity.TAG,"EEEE: "+response.errorBody().toString());
                /*Log.d(MainActivity.TAG, "Error is: "+response.body().getError().getMessage()+
                        " / "+response.body().getError().getCode()+
                        "\nSentBodyParameters: "+response.body().getSentBodyParameters().getSpotId()+
                        "\nSentHeaders: "+response.body().getSentHeaders().getContent_type()+
                        " / "+response.body().getSentHeaders().getToken());*/
                break;
        }
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

    public static void sendNetworkSpotAllRequest(Spot_All_Body body,String baseUrl,Context context){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KitesurfingAPI kitesurfingAPI = retrofit.create(KitesurfingAPI.class);

        Call<Spot_All_Result> call = kitesurfingAPI.getSpotAll(body);

        call.enqueue(new Callback<Spot_All_Result>() {
            @Override
            public void onResponse(Call<Spot_All_Result> call, Response<Spot_All_Result> response) {
                Log.d(MainActivity.TAG,"onResponse: the call on the api-spot-get-all endpoint has been succesful");
                Log.d(MainActivity.TAG,"onResponse: the code is: "+response.code());
                NetworkUtils.displayResponseGetAllSpot(response);
                if(response.code()==200){
                    //generate RecyclerView
                }
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

    public static void sendNetworkGetAllCountries(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KitesurfingAPI kitesurfingAPI = retrofit.create(KitesurfingAPI.class);

        Call<Spot_Countries_Result> call = kitesurfingAPI.getSpotCountries();
        call.enqueue(new Callback<Spot_Countries_Result>() {
            @Override
            public void onResponse(Call<Spot_Countries_Result> call, Response<Spot_Countries_Result> response) {
                Log.d(MainActivity.TAG,"onResponse: The call on the api-spot-get-countries endpoint has been succesful");
                displayResponseGetSpotCountries(response);
            }

            @Override
            public void onFailure(Call<Spot_Countries_Result> call, Throwable t) {
                Log.d(MainActivity.TAG,"onFailure: Something went wrong with the server");
                Log.d(MainActivity.TAG,t.toString());
            }
        });
    }

    public static void sendNetworkAddFavorites(Favorites_Add_Body body,String baseUrl,boolean isFavorited){
        if(isFavorited){
            Log.d(MainActivity.TAG,"Item with spotId: "+body.getSpotId()+" has already been favorited");
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KitesurfingAPI kitesurfingAPI = retrofit.create(KitesurfingAPI.class);

        Call<Favorites_Add_Result> call = kitesurfingAPI.getFavoritesAdd(body);
        call.enqueue(new Callback<Favorites_Add_Result>() {
            @Override
            public void onResponse(Call<Favorites_Add_Result> call, Response<Favorites_Add_Result> response) {
                Log.d(MainActivity.TAG,"onResponse: The call on the api-spot-favorites-add endpoint has been succesful");
                Log.d(MainActivity.TAG,"onResponse: "+response.toString());
                //displayResponseAddFavorites(response);
                Log.d(MainActivity.TAG,"onResponse: code is: "+response.code());
                displayResponseAddFavorites(response);
            }

            @Override
            public void onFailure(Call<Favorites_Add_Result> call, Throwable t) {
                Log.d(MainActivity.TAG,"onFailure: Something went wrong with the server");
                Log.d(MainActivity.TAG,t.toString());
            }
        });

    }

    public static void sendNetworkRemoveFavorites(Favorites_Remove_Body body, String baseUrl,boolean isRemoved){
        if(isRemoved){
            Log.d(MainActivity.TAG,"Item with spotId: "+body.getSpotId()+" has already been removed");
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KitesurfingAPI kitesurfingAPI = retrofit.create(KitesurfingAPI.class);

        Call<Favorites_Remove_Result> call = kitesurfingAPI.getFavoritesRemove(body);
        call.enqueue(new Callback<Favorites_Remove_Result>() {
            @Override
            public void onResponse(Call<Favorites_Remove_Result> call, Response<Favorites_Remove_Result> response) {
                Log.d(MainActivity.TAG,"onResponse: The call on the api-spot-favorites-remove endpoint has been succesful");
                Log.d(MainActivity.TAG,"onResponse: "+response.toString());
                //displayResponseAddFavorites(response);
                Log.d(MainActivity.TAG,"onResponse: code is: "+response.code());
                displayResponseRemoveFavorites(response);
            }

            @Override
            public void onFailure(Call<Favorites_Remove_Result> call, Throwable t) {
                Log.d(MainActivity.TAG,"onFailure: Something went wrong with the server");
                Log.d(MainActivity.TAG,t.toString());
            }
        });

    }

}
