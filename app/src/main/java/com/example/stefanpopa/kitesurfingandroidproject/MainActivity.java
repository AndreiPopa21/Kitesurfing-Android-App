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
           SpotsAdapter.SpotItemClickListener,
           SpotsAdapter.FavoriteStarClickListener,
            NetworkUtils.SpotChangeFavoriteState{

    public static final String TAG="MainActivity";
    //private static final String BASE_URL= "https://internship-2019.herokuapp.com";
    public static final String ALREADY_CALLED_FOR_LIST_KEY="already_called_for_list";
    public static final String ALREADY_CALLED_FOR_FAVORITE_CHANGE="already_called_for_favorite_change";
    public static final String FETCHED_DATA_FOR_LIST_KEY="fetched_data_for_list";
    public static final String IS_NO_CONNECTION_TEXT_VIEW_VISIBLE="is_no_connection_text_view_visible";
    public static final String IS_LIST_PROGRESS_BAR_VISIBLE="is_list_progress_bar_visible";

    public static final String SPOT_ID_KEY_FOR_THE_DETAIL_ACTIVITY="spot_id_key";
    public static final String SPOT_LOCATION_KEY_FOR_THE_DETAIL_ACTIVITY="spot_location_key";
    public static final String SPOT_INDEX_IN_MAIN_LIST="spot_index_in_main_list";

    public static final int FILTER_ACTIVITY_RESULT_CODE=100;
    public static final int DETAILS_ACTIVITY_RESULT_CODE=900;

    private Spot_All_Result spotsList;
    private boolean alreadyCalledForList=false;
    private boolean alreadyCalledForFavoriteChange=false;

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
        setAndCustomizeActionBar();
        NetworkUtils.allListFetchListener=this;
        NetworkUtils.spotChangeFavoriteStateListener=this;

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
                    performAllSpotsRequest("",0);
                }else{
                    noConnectionTextView.setVisibility(View.VISIBLE);
                    listProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
    private void setAndCustomizeActionBar(){
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

    private void checkSavedInstanceBundleContent(Bundle savedInstanceState){
        if(savedInstanceState.containsKey(ALREADY_CALLED_FOR_LIST_KEY)){
            this.alreadyCalledForList=savedInstanceState.getBoolean(ALREADY_CALLED_FOR_LIST_KEY);
        }else{
            Log.d(MainActivity.TAG,"BOOLEAN ALREADY CALLED FOR THE LIST IS NOT IN THE SAVED INSTANCE");
        }

        if(savedInstanceState.containsKey(ALREADY_CALLED_FOR_FAVORITE_CHANGE)){
            this.alreadyCalledForFavoriteChange=savedInstanceState.getBoolean(ALREADY_CALLED_FOR_FAVORITE_CHANGE);
        }else{
            Log.d(MainActivity.TAG,"BOOLEAN ALREADY CALLED FOR FAVORITE CHANGE IS NOT IN THE SAVED INSTANCE");
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

    private void createSpotsRecyclerView(Spot_All_Result spotsList){
        spotsRecyclerView.setHasFixedSize(true);
        spotsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        spotsAdapter=new SpotsAdapter(this,spotsList,this,this);
        spotsRecyclerView.setAdapter(spotsAdapter);
    }

    public void openDetailsActivity(String spotId, String location,int index_in_list){
        if(spotId==null || location==null){
            Toast.makeText(this,"Error opening Details",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent detailActivityStartIntent = new Intent(this,DetailActivity.class);
        detailActivityStartIntent.putExtra(SPOT_ID_KEY_FOR_THE_DETAIL_ACTIVITY,spotId);
        detailActivityStartIntent.putExtra(SPOT_LOCATION_KEY_FOR_THE_DETAIL_ACTIVITY,location);
        detailActivityStartIntent.putExtra(SPOT_INDEX_IN_MAIN_LIST,index_in_list);
        //startActivity(detailActivityStartIntent);
        startActivityForResult(detailActivityStartIntent,
                DETAILS_ACTIVITY_RESULT_CODE);
    }

    private void openFilterActivity(){
        Intent intent = new Intent(this,FilterActivity.class);
        startActivityForResult(intent,FILTER_ACTIVITY_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NetworkUtils.spotChangeFavoriteStateListener=this;
        if(data==null)
            return;

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
                Toast.makeText(this,"No filter applied",Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode==DETAILS_ACTIVITY_RESULT_CODE){
            if(resultCode==RESULT_OK){
                int index_in_main_list = data.
                        getIntExtra(DetailActivity.SPOT_DETAIL_RESULT_INDEX_IN_MAIN_LIST,-1);
                boolean isFav = data.
                        getBooleanExtra(DetailActivity.SPOT_DETAIL_RESULT_IS_FAVORITE,false);
                if(index_in_main_list == -1){
                    Toast.makeText(this,
                            "Error getting the right result from Details",
                            Toast.LENGTH_SHORT).show();
                }
                Log.d(MainActivity.TAG,"The values returned by the Details are: index: "+
                        String.valueOf(index_in_main_list)+" | isFav: "+String.valueOf(isFav));
                spotsList.getAll_result_children().get(index_in_main_list).setFavorite(isFav);
                spotsAdapter.setSpotsList(spotsList);
                spotsAdapter.notifyDataSetChanged();
            }
            if(requestCode==RESULT_CANCELED){
                Toast.makeText(this,"Error returning Details",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFavoriteStarClick(SpotsAdapter.SpotsViewHolder itemView) {
        if(alreadyCalledForFavoriteChange){
            Log.d(MainActivity.TAG,"A call to mark/unmark has already been established");
            Log.d(MainActivity.TAG,"Need to wait for response");
        }
        if(itemView!=null){
            if(itemView.isFavorite()){
                alreadyCalledForFavoriteChange=true;
                Log.d(MainActivity.TAG,"A call for REMOVE has been established");
                NetworkUtils.sendNetworkRemoveFavorites(new Favorites_Remove_Body(itemView.getId())
                        ,getString(R.string.base_url),itemView);
            }else{
                alreadyCalledForFavoriteChange=true;
                Log.d(MainActivity.TAG,"A call for ADD has been established");
                NetworkUtils.sendNetworkAddFavorites(new Favorites_Add_Body(itemView.getId()),
                        getString(R.string.base_url),itemView);
            }
        }else{
            Toast.makeText(this,"Invalid Favorite listener call",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSpotChangeFavoriteState(int result, SpotsAdapter.SpotsViewHolder itemView) {

        alreadyCalledForFavoriteChange=false;
        if(result==NetworkUtils.RESULT_ADDED_FAVORITE){
            Toast.makeText(this,"Added "
                    +itemView.getName()+" to favorites",Toast.LENGTH_SHORT).show();
            itemView.setFavorite(true);
            spotsList.getAll_result_children().
                    get(itemView.getIndex_in_list()).setFavorite(true);
            itemView.getFavoriteButton().
                    setBackground(ContextCompat.getDrawable(this,R.drawable.star_on));

        }
        if(result==NetworkUtils.RESULT_REMOVED_FAVORITE){
            Toast.makeText(this,"Removed "
                    +itemView.getName()+ "from favorites",Toast.LENGTH_SHORT).show();
            itemView.setFavorite(false);
            spotsList.getAll_result_children().
                    get(itemView.getIndex_in_list()).setFavorite(false);
            itemView.getFavoriteButton().
                    setBackground(ContextCompat.getDrawable(this,R.drawable.star_off));
        }
        if(result==NetworkUtils.RESULT_ERROR_CHANGE_STATE){
            Toast.makeText(this,"Error: Could not mark/unmark",Toast.LENGTH_SHORT).show();
            return;
        }
        return;
    }

    @Override
    public void onSpotsListFetcher(int result,Response<Spot_All_Result> list) {
        listProgressBar.setVisibility(View.INVISIBLE);
        noConnectionTextView.setVisibility(View.INVISIBLE);
        this.alreadyCalledForList=false;

        if(result==NetworkUtils.RESULT_RETURNED_ALL_LIST){
            this.spotsList=list.body();
            createSpotsRecyclerView(this.spotsList);
        }
        if(result==NetworkUtils.RESULT_ERROR_RETURNED_ALL_LIST){
            noConnectionTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSpotClick(String spotId,String location,int index_in_list) {
        openDetailsActivity(spotId,location,index_in_list);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(ALREADY_CALLED_FOR_LIST_KEY,this.alreadyCalledForList);
        outState.putBoolean(ALREADY_CALLED_FOR_FAVORITE_CHANGE,this.alreadyCalledForFavoriteChange);
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

}
