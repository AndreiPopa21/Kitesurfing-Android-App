package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_add_models.Favorites_Add_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_favorites_remove_models.Favorites_Remove_Body;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_details_models.Spot_Details_Body;

import org.w3c.dom.Text;

import java.io.Serializable;

public class DetailActivity extends AppCompatActivity
implements NetworkUtils.SpotDetailsFetcher,
           NetworkUtils.SpotChangeFavoriteState{

    public static final String DETAIL_TAG="DetailActivity";
    public static final String IS_DETAIL_NO_CONNECTION_TEXT_VIEW_VISIBLE="is_detail_no_connection_text_view_visible";
    public static final String IS_DETAIL_PROGRESS_BAR_VISIBLE="is_detail_progress_bar_visible";
    public static final String DETAIL_ON_SAVED_INSTANCE="detail_on_saved_instance";
    public static final String SPOT_DETAILS_OBJECT="spot_details_object";
    public static final String ALREADY_CALLED_FOR_DETAILS_KEY="already_called_for_list";
    public static final String DETAIL_ALREADY_CALLED_FOR_FAVORITE_CHANGE="detail_already_favorite_change";

    public static final String SPOT_DETAIL_ID="spot_detail_id";
    public static final String SPOT_DETAIL_LOCATION="spot_detail_location";
    public static final String SPOT_DETAIL_INDEX_IN_MAIN_LIST="spot_detail_index_in_main_list";

    public static final String SPOT_DETAIL_RESULT_IS_FAVORITE="spot_detail_result_is_favorite";
    public static final String SPOT_DETAIL_RESULT_INDEX_IN_MAIN_LIST="spot_detail_result_index_in_main_list";

    private ProgressBar detailProgressBar;
    private TextView detailNoConnectionTextView;

    private LinearLayout nameLinearLayout;
    private LinearLayout countryLinearLayout;
    private LinearLayout windProbabilityLinearLayout;
    private LinearLayout whenToGoLineaLayout;
    private LinearLayout longitudeLinearLayout;
    private LinearLayout latitudeLinearLayout;

    private TextView nameTextView;
    private TextView countryTextView;
    private TextView windProbabilityTextView;
    private TextView whenToGoTextView;
    private TextView longitudeTextView;
    private TextView latitudeTextView;

    private Button viewLocationButton;

    private TextView actionBarNameTextView;
    private Button detailRefreshButton;
    private Button detailFavoriteButton;

    //boolean that tells us whether a configuration change took place
    private boolean detailOnSavedInstance=false;
    private boolean alreadyCalledForDetails=false;
    private boolean alreadyCalledForFavoriteChange=false;

    //Data passed via Intent
    private String spotId;
    private String spotLocation;
    private boolean spotIsFavorite=false;
    private int spotIndexInMainList;

    private SpotDetails spotDetails=null;//object that holds all details about our spot

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        NetworkUtils.spotDetailsFetchListener=this;
        NetworkUtils.spotChangeFavoriteStateListener=this;
        setCustomActionBar();
        bindViews();

        if(savedInstanceState==null){
            Intent intentThatStartedActivity = getIntent();
            if(intentThatStartedActivity==null){
                closeOnError(getString(R.string.passed_empty_intent_to_detail_toast_text));
            }
            if(!checkIntentContent(intentThatStartedActivity)){
                closeOnError(getString(R.string.extras_do_not_follow_format_toast_text));
            }
            spotDetails=null;
        }else {
            checkViewsVisibility(savedInstanceState);
            checkSavedInstanceBundleContent(savedInstanceState);
        }
        Log.d(DetailActivity.DETAIL_TAG,"SpotId is: "+this.spotId+"| Location is: "+this.spotLocation);
        customizeActionBar();

        if(!alreadyCalledForDetails){
            if(spotDetails==null){
                if(NetworkUtils.isNetworkAvailable(this)){
                    alreadyCalledForDetails=true;
                    performSpotDetailRequest(spotId);
                }else{
                    detailNoConnectionTextView.setVisibility(View.VISIBLE);
                    detailProgressBar.setVisibility(View.INVISIBLE);
                }
            }else{
                Log.d(DetailActivity.DETAIL_TAG,"Another call is not necessary, details have already been received");
                populateLayout(this.spotDetails);
            }
        }
    }
    private void bindViews(){
        detailProgressBar=(ProgressBar)findViewById(R.id.detail_progress_bar);
        detailNoConnectionTextView=(TextView)findViewById(R.id.detail_no_connection_text_view);

        nameLinearLayout=(LinearLayout)findViewById(R.id.detail_name_linear_layout);
        countryLinearLayout=(LinearLayout)findViewById(R.id.detail_country_linear_layout);
        windProbabilityLinearLayout=(LinearLayout)findViewById(R.id.detail_wind_probability_linear_layout);
        whenToGoLineaLayout=(LinearLayout)findViewById(R.id.detail_when_to_go_linear_layout);
        latitudeLinearLayout = (LinearLayout)findViewById(R.id.detail_latitude_linear_layout);
        longitudeLinearLayout=(LinearLayout)findViewById(R.id.detail_longitude_linear_layout);

        nameTextView=(TextView)findViewById(R.id.name_text_view);
        countryTextView=(TextView)findViewById(R.id.country_text_view);
        windProbabilityTextView=(TextView)findViewById(R.id.wind_probability_text_view);
        whenToGoTextView=(TextView)findViewById(R.id.when_to_go_text_view);
        longitudeTextView=(TextView)findViewById(R.id.longitude_text_view);
        latitudeTextView=(TextView)findViewById(R.id.latitude_text_view);

        viewLocationButton=(Button)findViewById(R.id.view_location_button);

        actionBarNameTextView=(TextView)getSupportActionBar().getCustomView()
                .findViewById(R.id.action_bar_spot_name_text_view);
        detailRefreshButton=(Button)getSupportActionBar().getCustomView()
                .findViewById(R.id.detail_refresh_button);
        detailFavoriteButton=(Button)getSupportActionBar().getCustomView()
                .findViewById(R.id.detail_favorite_star_button);

        setViewsInvisible();
    }
    private void setCustomActionBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
    }
    private void customizeActionBar(){

        actionBarNameTextView.setText(spotLocation);
        detailRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDetails();
            }
        });
        detailFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markFavorite();
            }
        });
    }
    private void populateLayout(final SpotDetails spotDetails){
        nameLinearLayout.setVisibility(View.VISIBLE);
        nameTextView.setText(spotDetails.getName());

        countryLinearLayout.setVisibility(View.VISIBLE);
        countryTextView.setText(spotDetails.getCountry());

        windProbabilityLinearLayout.setVisibility(View.VISIBLE);
        windProbabilityTextView.setText(String.valueOf(spotDetails.getWindProbability()));

        whenToGoLineaLayout.setVisibility(View.VISIBLE);
        whenToGoTextView.setText(spotDetails.getWhenToGo());

        latitudeLinearLayout.setVisibility(View.VISIBLE);
        latitudeTextView.setText(String.valueOf(spotDetails.getLatitude()));

        longitudeLinearLayout.setVisibility(View.VISIBLE);
        longitudeTextView.setText(String.valueOf(spotDetails.getLongitude()));

        viewLocationButton.setEnabled(true);
        viewLocationButton.setVisibility(View.VISIBLE);
        viewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri_location = "geo:"
                        +String.valueOf(spotDetails.getLatitude())
                        +","+String.valueOf(spotDetails.getLongitude());
                Uri gmmIntentUri = Uri.parse(uri_location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,gmmIntentUri);
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        detailFavoriteButton.setEnabled(true);
        if(spotDetails.isFavorite()){
            spotIsFavorite=true;
            detailFavoriteButton.
                    setBackground(ContextCompat.getDrawable(this,R.drawable.star_on));
            Log.d(DetailActivity.DETAIL_TAG,"SpotIsFavorite: true");
        }else{
            spotIsFavorite=false;
            detailFavoriteButton.
                    setBackground(ContextCompat.getDrawable(this,R.drawable.star_off));
            Log.d(DetailActivity.DETAIL_TAG,"SpotIsFavorite: false");
        }
    }
    private void setViewsInvisible(){

        detailProgressBar.setVisibility(View.INVISIBLE);
        detailNoConnectionTextView.setVisibility(View.INVISIBLE);

        nameLinearLayout.setVisibility(View.INVISIBLE);
        countryLinearLayout.setVisibility(View.INVISIBLE);
        windProbabilityLinearLayout.setVisibility(View.INVISIBLE);
        whenToGoLineaLayout.setVisibility(View.INVISIBLE);
        latitudeLinearLayout.setVisibility(View.INVISIBLE);
        longitudeLinearLayout.setVisibility(View.INVISIBLE);

        viewLocationButton.setVisibility(View.INVISIBLE);
        viewLocationButton.setEnabled(false);

        detailFavoriteButton.setEnabled(false);
        detailFavoriteButton.
                setBackground(ContextCompat.getDrawable(this,R.drawable.star_off));
        spotIsFavorite=false;
    }

    private void refreshDetails(){
        Log.d(DetailActivity.DETAIL_TAG,"Pressed Refresh button");
        if(NetworkUtils.isNetworkAvailable(getApplicationContext())){
            alreadyCalledForDetails=true;
            spotDetails=null;
            setViewsInvisible();
            performSpotDetailRequest(spotId);
        }else{
            Toast.makeText(getApplicationContext(),
                    getString(R.string.no_internet_connection_text),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void markFavorite(){
        if(alreadyCalledForFavoriteChange)
            return;

        if(NetworkUtils.isNetworkAvailable(this)){
            detailProgressBar.setVisibility(View.VISIBLE);
            if(spotIsFavorite){
                alreadyCalledForFavoriteChange=true;
                Log.d(MainActivity.TAG,"A call for REMOVE has been established");
                NetworkUtils.sendNetworkRemoveFavorites(new Favorites_Remove_Body(spotId)
                        ,getString(R.string.base_url),null);
            }else{
                alreadyCalledForFavoriteChange=true;
                Log.d(MainActivity.TAG,"A call for ADD has been established");
                NetworkUtils.sendNetworkAddFavorites(new Favorites_Add_Body(spotId),
                        getString(R.string.base_url),null);
            }
        }else{
            Log.d(DetailActivity.DETAIL_TAG,"Tried mark/unmark, but no internet connection");
            Toast.makeText(this,
                            getString(R.string.no_internet_connection_text),
                            Toast.LENGTH_SHORT).show();
        }
    }

    private void closeOnError(String errorMessage){
        Log.e(DetailActivity.DETAIL_TAG,"Error: "+errorMessage);
        finish();
    }

    private boolean checkIntentContent(Intent intent){
        Bundle extras= intent.getExtras();
        if(extras==null){
            return false;
        }
        if(!extras.containsKey(MainActivity.SPOT_ID_KEY_FOR_THE_DETAIL_ACTIVITY))
            closeOnError(getString(R.string.intent_not_containing_spotid_error_text));
        if(!extras.containsKey(MainActivity.SPOT_LOCATION_KEY_FOR_THE_DETAIL_ACTIVITY))
            closeOnError(getString(R.string.intent_not_containing_spotlocation_error_text));
        if(!extras.containsKey(MainActivity.SPOT_INDEX_IN_MAIN_LIST_KEY))
            closeOnError(getString(R.string.intent_not_containing_spot_index_error_text));

        spotLocation = extras.getString(MainActivity.SPOT_LOCATION_KEY_FOR_THE_DETAIL_ACTIVITY);
        spotId= extras.getString(MainActivity.SPOT_ID_KEY_FOR_THE_DETAIL_ACTIVITY);
        spotIndexInMainList=extras.getInt(MainActivity.SPOT_INDEX_IN_MAIN_LIST_KEY);
        if(spotLocation==null || spotId==null){
            return false;
        }
        return true;
    }

    private void checkSavedInstanceBundleContent(Bundle savedInstanceState){
        if(savedInstanceState.containsKey(ALREADY_CALLED_FOR_DETAILS_KEY)){
            alreadyCalledForDetails=savedInstanceState.getBoolean(ALREADY_CALLED_FOR_DETAILS_KEY);
        }else{
            alreadyCalledForDetails=false;
        }

        if(savedInstanceState.containsKey(DETAIL_ALREADY_CALLED_FOR_FAVORITE_CHANGE)){
            alreadyCalledForFavoriteChange=savedInstanceState.
                    getBoolean(DETAIL_ALREADY_CALLED_FOR_FAVORITE_CHANGE);
        }else{
            alreadyCalledForFavoriteChange=false;
        }

        if(savedInstanceState.containsKey(SPOT_DETAILS_OBJECT)){
            spotDetails=(SpotDetails)savedInstanceState.getSerializable(SPOT_DETAILS_OBJECT);
            spotIsFavorite=spotDetails.isFavorite();
        }else{
            spotDetails=null;
            spotIsFavorite=false;
        }

        if(savedInstanceState.containsKey(SPOT_DETAIL_ID)){
            spotId=savedInstanceState.getString(SPOT_DETAIL_ID);
        }else{
            spotId=null;
            closeOnError(getString(R.string.problem_getting_spotid_saved_text));
        }

        if(savedInstanceState.containsKey(SPOT_DETAIL_LOCATION)){
            spotLocation=savedInstanceState.getString(SPOT_DETAIL_LOCATION);
        }else{
            spotLocation=null;
            closeOnError(getString(R.string.problem_getting_spot_location_error_text));
        }

        if(savedInstanceState.containsKey(SPOT_DETAIL_INDEX_IN_MAIN_LIST)){
            spotIndexInMainList=savedInstanceState.getInt(SPOT_DETAIL_INDEX_IN_MAIN_LIST);
        }else{
            spotIndexInMainList=0;
            closeOnError(getString(R.string.problem_getting_spot_index_error_text));
        }
    }

    private void checkViewsVisibility(Bundle savedInstanceState){
        if(savedInstanceState.containsKey(IS_DETAIL_NO_CONNECTION_TEXT_VIEW_VISIBLE)){
            if(savedInstanceState.getBoolean(IS_DETAIL_NO_CONNECTION_TEXT_VIEW_VISIBLE)){
                detailNoConnectionTextView.setVisibility(View.VISIBLE);
            }else{
                detailNoConnectionTextView.setVisibility(View.INVISIBLE);
            }

            if(savedInstanceState.getBoolean(IS_DETAIL_PROGRESS_BAR_VISIBLE)){
                detailProgressBar.setVisibility(View.VISIBLE);
            }else{
                detailProgressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void performSpotDetailRequest(String spotId){
        if(spotId==null){
            return;
        }
        Log.d(DetailActivity.DETAIL_TAG,"A call for details for ID: "+spotId+" has been established");
        detailProgressBar.setVisibility(View.VISIBLE);
        detailNoConnectionTextView.setVisibility(View.INVISIBLE);
        NetworkUtils.sendNetworkSpotDetailsRequest(new Spot_Details_Body(spotId),getString(R.string.base_url));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        detailOnSavedInstance=true;
        outState.putBoolean(DETAIL_ON_SAVED_INSTANCE,detailOnSavedInstance);

        if(detailProgressBar.getVisibility()==View.VISIBLE){
            outState.putBoolean(IS_DETAIL_PROGRESS_BAR_VISIBLE,true);
        }else{
            outState.putBoolean(IS_DETAIL_PROGRESS_BAR_VISIBLE,false);
        }

        if(detailNoConnectionTextView.getVisibility()==View.VISIBLE){
            outState.putBoolean(IS_DETAIL_NO_CONNECTION_TEXT_VIEW_VISIBLE,true);
        }else{
            outState.putBoolean(IS_DETAIL_NO_CONNECTION_TEXT_VIEW_VISIBLE,false);
        }

        outState.putBoolean(ALREADY_CALLED_FOR_DETAILS_KEY,alreadyCalledForDetails);

        outState.putBoolean(DETAIL_ALREADY_CALLED_FOR_FAVORITE_CHANGE,alreadyCalledForFavoriteChange);

        outState.putSerializable(SPOT_DETAILS_OBJECT,spotDetails);

        outState.putString(SPOT_DETAIL_ID,spotId);

        outState.putString(SPOT_DETAIL_LOCATION,spotLocation);

        outState.putInt(SPOT_DETAIL_INDEX_IN_MAIN_LIST,spotIndexInMainList);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSpotDetailsFetcher(int result,SpotDetails spotDetails) {
        this.alreadyCalledForDetails=false;
        this.spotDetails=spotDetails;
        if(result==NetworkUtils.RESULT_DETAILS_RETURNED){
            detailProgressBar.setVisibility(View.INVISIBLE);
            detailNoConnectionTextView.setVisibility(View.INVISIBLE);
            Log.d(DetailActivity.DETAIL_TAG,"Details have been succesfully received");
            Toast.makeText(this,getString(R.string.details_succesfully_received_toast_text),Toast.LENGTH_SHORT).show();
            populateLayout(spotDetails);
        }
        if(result==NetworkUtils.RESULT_ERROR_DETAILS_RETURNED){
            detailProgressBar.setVisibility(View.INVISIBLE);
            detailNoConnectionTextView.setVisibility(View.INVISIBLE);
            Log.d(DetailActivity.DETAIL_TAG,"Details have not been succesfully received");
            Toast.makeText(this,getString(R.string.details_not_succesfully_received_toast_text),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(alreadyCalledForFavoriteChange)
            return;
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SPOT_DETAIL_RESULT_IS_FAVORITE,spotIsFavorite);
        resultIntent.putExtra(SPOT_DETAIL_RESULT_INDEX_IN_MAIN_LIST,spotIndexInMainList);
        setResult(RESULT_OK,resultIntent);
        Log.d(DetailActivity.DETAIL_TAG,"Back button was pressed");
        //Toast.makeText(this,"Back button pressed",Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }

    @Override
    public void onSpotChangeFavoriteState(int result, SpotsAdapter.SpotsViewHolder itemView) {
        alreadyCalledForFavoriteChange=false;
        detailProgressBar.setVisibility(View.INVISIBLE);
        if(result==NetworkUtils.RESULT_ADDED_FAVORITE){
            Toast.makeText(this,getString(R.string.added_to_favorites_toast_text),Toast.LENGTH_SHORT).show();
            detailFavoriteButton.setBackground(ContextCompat.getDrawable(this,R.drawable.star_on));
            spotIsFavorite=true;
            spotDetails.setFavorite(true);
        }
        if(result==NetworkUtils.RESULT_REMOVED_FAVORITE){
            Toast.makeText(this,getString(R.string.removed_from_favorites_toast_text),Toast.LENGTH_SHORT).show();
            detailFavoriteButton.setBackground(ContextCompat.getDrawable(this,R.drawable.star_off));
            spotIsFavorite=false;
            spotDetails.setFavorite(false);
        }
        if(result==NetworkUtils.RESULT_ERROR_CHANGE_STATE){
            Toast.makeText(this,getString(R.string.could_not_mark_unmark_toast_text),Toast.LENGTH_SHORT).show();
            return;
        }
        return;
    }
}
