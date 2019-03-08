package com.example.stefanpopa.kitesurfingandroidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_details_models.Spot_Details_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_user_get_models.Auth_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_user_get_models.Auth_Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG="MainActivity";
    //private static final String BASE_URL= "https://internship-2019.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Auth_Body auth = new Auth_Body(getString(R.string.valid_email));
        //Spot_All_Body spot = new Spot_All_Body(null,70);
        Spot_Details_Body details = new Spot_Details_Body("bz1vaqsrgq");
        NetworkUtils.sendNetworkSpotDetailsRequest(details,getString(R.string.base_url));
       // NetworkUtils.sendNetworkSpotAllRequest(new Spot_All_Body(null,0),getString(R.string.base_url));
       // sendNetworkAuthRequest(spot);
    }

    private void sendNetworkAuthRequest(Spot_All_Body spot){

    }
}
