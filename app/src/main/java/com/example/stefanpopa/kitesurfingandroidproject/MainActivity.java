package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_add_models.Favorites_Add_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_remove_models.Favorites_Remove_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_details_models.Spot_Details_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_user_get_models.Auth_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_user_get_models.Auth_Result;

import java.nio.charset.MalformedInputException;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
implements NetworkUtils.SpotsListFetcher,
           NetworkUtils.ReceiveInternetConnection,
           SpotsAdapter.SpotItemClickListener{

    public static final String TAG="MainActivity";
    //private static final String BASE_URL= "https://internship-2019.herokuapp.com";
        public static final String ALREADY_CALLED_FOR_LIST_KEY="already_called_for_list";
    public static final String FETCHED_DATA_FOR_LIST_KEY="fetched_data_for_list";
    public static final String IS_NO_CONNECTION_TEXT_VIEW_VISIBLE="is_no_connection_text_view_visible";
    public static final String IS_LIST_PROGRESS_BAR_VISIBLE="is_list_progress_bar_visible";
    public static final String SPOT_ID_KEY_FOR_THE_DETAIL_ACTIVITY="spot_id_key";
    public static final String SPOT_LOCATION_KEY_FOR_THE_DETAIL_ACTIVITY="spot_location_key";

    private Spot_All_Result spotsList;
    private boolean alreadyCalledForList=false;

    private RecyclerView spotsRecyclerView;
    private ProgressBar listProgressBar;
    private TextView noConnectionTextView;

    private SpotsAdapter spotsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spotsRecyclerView=(RecyclerView)findViewById(R.id.spots_recycler_view);
        listProgressBar=(ProgressBar)findViewById(R.id.list_progress_bar);
        noConnectionTextView=(TextView)findViewById(R.id.no_connection_text_view);

        NetworkUtils.allListFetchListener=this;

        Log.d(MainActivity.TAG,"Check network connectivity: "+NetworkUtils.isNetworkAvailable(this));
        //checkNetworkEverySeconds(5000);
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

        if(savedInstanceState==null){
            Log.d(MainActivity.TAG,"THERE IS NOTHING IN THE SAVED INSTANCE!");
        }else{
            Log.d(MainActivity.TAG,"THERE IS SOMETHING IN THE SAVED INSTANCE!");
            if(savedInstanceState.containsKey(ALREADY_CALLED_FOR_LIST_KEY)){
                this.alreadyCalledForList=savedInstanceState.getBoolean(ALREADY_CALLED_FOR_LIST_KEY);
            }else{
                Log.d(MainActivity.TAG,"BOOLEAN ALREADY CALLED FOR THE LIST IS NOT IN THE SAVED INSTANCE");
            }

            if(savedInstanceState.containsKey(FETCHED_DATA_FOR_LIST_KEY)){
                this.spotsList=(Spot_All_Result)savedInstanceState.getSerializable(FETCHED_DATA_FOR_LIST_KEY);
                createSpotsRecyclerView(this.spotsList);
            }else{
                Log.d(MainActivity.TAG,"SERIALIZABLE SPOTS LIST IS NOT IN THE SAVED INSTANCE");
            }

            if(savedInstanceState.containsKey(IS_NO_CONNECTION_TEXT_VIEW_VISIBLE)){
                boolean visible = savedInstanceState.getBoolean(IS_NO_CONNECTION_TEXT_VIEW_VISIBLE);
                if(visible){
                    noConnectionTextView.setVisibility(View.VISIBLE);
                }else{
                    noConnectionTextView.setVisibility(View.INVISIBLE);
                }
            }

            if(savedInstanceState.containsKey(IS_LIST_PROGRESS_BAR_VISIBLE)){
                boolean visible = savedInstanceState.getBoolean(IS_LIST_PROGRESS_BAR_VISIBLE);
                if(visible){
                    listProgressBar.setVisibility(View.VISIBLE);
                }else{
                    listProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        }

        if(!alreadyCalledForList) {
            if(this.spotsList==null){
                if(NetworkUtils.isNetworkAvailable(this)){
                    alreadyCalledForList=true;
                    //checkNetworkEverySeconds(5000);
                    performAllSpotsRequest();
                }else{
                    noConnectionTextView.setVisibility(View.VISIBLE);
                    listProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void performAllSpotsRequest(){
        Log.d(MainActivity.TAG,"A call for the list has been established");
        listProgressBar.setVisibility(View.VISIBLE);
        noConnectionTextView.setVisibility(View.INVISIBLE);
        NetworkUtils.sendNetworkSpotAllRequest(new Spot_All_Body("", 0), getString(R.string.base_url));
    }
   /*
    private void checkNetworkEverySeconds(int miliseconds){
        Timer timer = new Timer();
        final int MILLISECONDS = miliseconds;
        timer.schedule(new CheckConnection(this,3,this), 0, MILLISECONDS);

    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(ALREADY_CALLED_FOR_LIST_KEY,this.alreadyCalledForList);
        outState.putSerializable(FETCHED_DATA_FOR_LIST_KEY,this.spotsList);
        if(listProgressBar.getVisibility()==View.VISIBLE){
            outState.putBoolean(IS_LIST_PROGRESS_BAR_VISIBLE,true);
        }else{
            outState.putBoolean(IS_LIST_PROGRESS_BAR_VISIBLE,false);
        }

        if(noConnectionTextView.getVisibility()==View.VISIBLE){
            outState.putBoolean(IS_NO_CONNECTION_TEXT_VIEW_VISIBLE,true);
        }else{
            outState.putBoolean(IS_NO_CONNECTION_TEXT_VIEW_VISIBLE,false);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSpotsListFetcher(Response<Spot_All_Result> list) {
        listProgressBar.setVisibility(View.INVISIBLE);
        noConnectionTextView.setVisibility(View.INVISIBLE);
        this.spotsList=list.body();
        this.alreadyCalledForList=false;
        createSpotsRecyclerView(this.spotsList);
        //Log.d(MainActivity.TAG,"RADARADARAD A MERRRRS");
        //NetworkUtils.displayResponseGetAllSpot(this.spotsList);
    }

    private void createSpotsRecyclerView(Spot_All_Result spotsList){
        spotsRecyclerView.setHasFixedSize(true);
        spotsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        spotsAdapter=new SpotsAdapter(this,spotsList,this);
        spotsRecyclerView.setAdapter(spotsAdapter);
    }

    @Override
    public void onReceivedInternetConnection(boolean status) {
        if(status){
            //we have internet connection
        }else{
           // Toast.makeText(getParent().getBaseContext(),"No Internet",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSpotClick(String spotId,String location) {
        //Toast.makeText(this,spotId,Toast.LENGTH_SHORT).show();
        if(spotId==null || location==null){
            Toast.makeText(this,"Error checking the details",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent detailActivityStartIntent = new Intent(this,DetailActivity.class);
        detailActivityStartIntent.putExtra(SPOT_ID_KEY_FOR_THE_DETAIL_ACTIVITY,spotId);
        detailActivityStartIntent.putExtra(SPOT_LOCATION_KEY_FOR_THE_DETAIL_ACTIVITY,location);
        startActivity(detailActivityStartIntent);
    }
}
