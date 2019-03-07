package com.example.stefanpopa.kitesurfingandroidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.stefanpopa.kitesurfingandroidproject.json_models.api_get_user_models.Auth_Body;
import com.example.stefanpopa.kitesurfingandroidproject.json_models.api_get_user_models.Auth_Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    //private static final String BASE_URL= "https://internship-2019.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Auth_Body auth = new Auth_Body(getString(R.string.valid_email));
        sendNetworkAuthRequest(auth);
    }

    private void sendNetworkAuthRequest(Auth_Body auth){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KitesurfingAPI kitesurfingAPI = retrofit.create(KitesurfingAPI.class);

        Call<Auth_Result> call = kitesurfingAPI.getAuth(auth);
        call.enqueue(new Callback<Auth_Result>() {
            @Override
            public void onResponse(Call<Auth_Result> call, Response<Auth_Result> response) {
                Log.d(TAG,"onResponse: Server Response is: "+response.toString());
                Log.d(TAG,"onResponse: Server Response Token is: "+response.body().getResult_children().getToken());
                Log.d(TAG,"onResponse: Server Response Email is: "+response.body().getResult_children().getEmail());
                //Toast.makeText(MainActivity.this,response.body().getToken(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Auth_Result> call, Throwable t) {
                Log.d(TAG,"onFailure: Something went wrong with the server");
            }
        });
    }
}
