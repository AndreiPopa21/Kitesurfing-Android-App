package com.example.stefanpopa.kitesurfingandroidproject;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_add_models.Favorites_Add_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_remove_models.Favorites_Remove_Body;
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

public class MainActivity extends AppCompatActivity
implements NetworkUtils.SpotsListFetcher{

    public static final String TAG="MainActivity";
    //private static final String BASE_URL= "https://internship-2019.herokuapp.com";

    private Response<Spot_All_Result> spotsList;

    private RecyclerView spotsRecyclerView;
    private SpotsAdapter spotsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spotsRecyclerView=(RecyclerView)findViewById(R.id.spots_recycler_view);
        NetworkUtils.allListFetchListener=this;
        //Auth_Body auth = new Auth_Body(getString(R.string.valid_email));
        //Spot_All_Body spot = new Spot_All_Body(null,70);
        //Spot_Details_Body details = new Spot_Details_Body("bz1vaqsrgq");
        //NetworkUtils.sendNetworkSpotDetailsRequest(details,getString(R.string.base_url));
       // NetworkUtils.sendNetworkSpotAllRequest(new Spot_All_Body(null,0),getString(R.string.base_url));
       // sendNetworkAuthRequest(spot);
        //NetworkUtils.sendNetworkGetAllCountries(getString(R.string.base_url));
        //NetworkUtils.sendNetworkAddFavorites(new Favorites_Add_Body("hfjlTbb4NC"),getString(R.string.base_url));
        //NetworkUtils.sendNetworkRemoveFavorites(new Favorites_Remove_Body("hfjlTbb4NC"),getString(R.string.base_url));
        //NetworkUtils.sendNetworkSpotAllRequest(new Spot_All_Body("",0),getString(R.string.base_url));
        NetworkUtils.sendNetworkSpotAllRequest(new Spot_All_Body("Morocco",20),getString(R.string.base_url));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onSpotsListFetcher(Response<Spot_All_Result> list) {
        this.spotsList=list;
        createSpotsRecyclerView(list);
        //Log.d(MainActivity.TAG,"RADARADARAD A MERRRRS");
        //NetworkUtils.displayResponseGetAllSpot(this.spotsList);
    }

    private void createSpotsRecyclerView(Response<Spot_All_Result> list){
        spotsRecyclerView.setHasFixedSize(true);
        spotsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        spotsAdapter=new SpotsAdapter(this,list);
        spotsRecyclerView.setAdapter(spotsAdapter);
    }
}
