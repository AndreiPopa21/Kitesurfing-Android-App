package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
           SpotsAdapter.SpotItemClickListener,
           SpotsAdapter.FavoriteStarClickListener {

    public static final String TAG="MainActivity";
    //private static final String BASE_URL= "https://internship-2019.herokuapp.com";
    public static final String ALREADY_CALLED_FOR_LIST_KEY="already_called_for_list";
    public static final String FETCHED_DATA_FOR_LIST_KEY="fetched_data_for_list";
    public static final String IS_NO_CONNECTION_TEXT_VIEW_VISIBLE="is_no_connection_text_view_visible";
    public static final String IS_LIST_PROGRESS_BAR_VISIBLE="is_list_progress_bar_visible";
    public static final String SPOT_ID_KEY_FOR_THE_DETAIL_ACTIVITY="spot_id_key";
    public static final String SPOT_LOCATION_KEY_FOR_THE_DETAIL_ACTIVITY="spot_location_key";

    public static final int FILTER_ACTIVITY_RESULT_CODE=100;

    private Spot_All_Result spotsList;
    private boolean alreadyCalledForList=false;

    private RecyclerView spotsRecyclerView;
    private ProgressBar listProgressBar;
    private TextView noConnectionTextView;
    private Button mainRefreshButton;
    private Button filterButton;

    private SpotsAdapter spotsAdapter;

    private int windProbability=0;
    private String country="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        customizeActionBar();
        NetworkUtils.allListFetchListener=this;

        Log.d(MainActivity.TAG,"Check network connectivity: "+NetworkUtils.isNetworkAvailable(this));
        //checkNetworkEverySeconds(5000);

        if(savedInstanceState==null){
            Log.d(MainActivity.TAG,"THERE IS NOTHING IN THE SAVED INSTANCE!");
        }else{
            Log.d(MainActivity.TAG,"THERE IS SOMETHING IN THE SAVED INSTANCE!");
            checkSavedInstanceBundleContent(savedInstanceState);
        }

        if(!alreadyCalledForList) {
            if(this.spotsList==null){
                if(NetworkUtils.isNetworkAvailable(this)){
                    //checkNetworkEverySeconds(5000);
                    performAllSpotsRequest("",0);
                }else{
                    noConnectionTextView.setVisibility(View.VISIBLE);
                    listProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
    private void customizeActionBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.main_custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();
        mainRefreshButton=(Button)view.findViewById(R.id.main_refresh_button);
        mainRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.TAG,"Pressed Refresh button");
                if(NetworkUtils.isNetworkAvailable(getApplicationContext())){
                    setViewsInvisible();
                    performAllSpotsRequest("",0);
                }else{
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
        filterButton=(Button)findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterActivity();
            }
        });
    }

    private void bindViews(){
        spotsRecyclerView=(RecyclerView)findViewById(R.id.spots_recycler_view);
        listProgressBar=(ProgressBar)findViewById(R.id.list_progress_bar);
        noConnectionTextView=(TextView)findViewById(R.id.no_connection_text_view);
    }

    private void openFilterActivity(){
        Intent intent = new Intent(this,FilterActivity.class);
        startActivityForResult(intent,FILTER_ACTIVITY_RESULT_CODE);
    }

    private void checkSavedInstanceBundleContent(Bundle savedInstanceState){
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

    private void performAllSpotsRequest(String country, int windProbability){
        alreadyCalledForList=true;
        spotsList=null;
        Log.d(MainActivity.TAG,"A call for the list has been established");
        listProgressBar.setVisibility(View.VISIBLE);
        noConnectionTextView.setVisibility(View.INVISIBLE);
        NetworkUtils.sendNetworkSpotAllRequest(new Spot_All_Body(country, windProbability),
                                                getString(R.string.base_url));
    }


    private void setViewsInvisible(){
        listProgressBar.setVisibility(View.INVISIBLE);
        noConnectionTextView.setVisibility(View.INVISIBLE);
        spotsRecyclerView.setAdapter(null);
    }

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
        spotsAdapter=new SpotsAdapter(this,spotsList,this,this);
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

      /*
    private void checkNetworkEverySeconds(int miliseconds){
        Timer timer = new Timer();
        final int MILLISECONDS = miliseconds;
        timer.schedule(new CheckConnection(this,3,this), 0, MILLISECONDS);

    }*/

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(MainActivity.TAG,"Entered onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(MainActivity.TAG,"Entered onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(MainActivity.TAG,"Entered onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(MainActivity.TAG,"Entered onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(MainActivity.TAG,"Entered onDestroy()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILTER_ACTIVITY_RESULT_CODE){
            if(resultCode==RESULT_OK){
                country=data.getStringExtra(FilterActivity.COUNTRY_EDIT_TEXT_KEY);
                windProbability=data.getIntExtra(FilterActivity.WIND_PROBABILITY_EDIT_TEXT_KEY,0);

                Log.d(MainActivity.TAG,"Country: "+country
                        +"| WindProbability: "+String.valueOf(windProbability));
                if(NetworkUtils.isNetworkAvailable(this)){
                    performAllSpotsRequest(country,windProbability);
                }else{
                    setViewsInvisible();
                    noConnectionTextView.setVisibility(View.VISIBLE);
                }
            }
            if(resultCode==RESULT_CANCELED){
                Toast.makeText(this,"Error processing the Filter result",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFavoriteStarClick(SpotsAdapter.SpotsViewHolder itemView) {
        if(itemView!=null){
            //Toast.makeText(this,itemView.getName(),Toast.LENGTH_SHORT).show();
            itemView.setFavorite(!itemView.isFavorite());
            if(itemView.isFavorite()){
                itemView.getFavoriteButton().setBackground(ContextCompat.getDrawable(this,R.drawable.star_on));
            }else{
                itemView.getFavoriteButton().setBackground(ContextCompat.getDrawable(this,R.drawable.star_off));
            }
        }else{
            Toast.makeText(this,"Invalid Favorite listener call",Toast.LENGTH_SHORT).show();
        }
    }
}
