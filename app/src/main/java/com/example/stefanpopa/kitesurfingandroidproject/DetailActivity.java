package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_details_models.Spot_Details_Body;

import java.io.Serializable;

public class DetailActivity extends AppCompatActivity
implements NetworkUtils.SpotDetailFetcher {

    public static final String DETAIL_TAG="DetailActivity";
    public static final String IS_DETAIL_NO_CONNECTION_TEXT_VIEW_VISIBLE="is_detail_no_connection_text_view_visible";
    public static final String IS_DETAIL_PROGRESS_BAR_VISIBLE="is_detail_progress_bar_visible";
    public static final String DETAIL_ON_SAVED_INSTANCE="detail_on_saved_instance";
    public static final String SPOT_DETAILS_OBJECT="spot_details_object";
    public static final String ALREADY_CALLED_FOR_DETAILS_KEY="already_called_for_list";
    public static final String SPOT_DETAIL_ID="spot_detail_id";
    public static final String SPOT_DETAIL_LOCATION="spot_detail_location";


    private ProgressBar detailProgressBar;
    private TextView detailNoConnectionTextView;

    //boolean that tells us whether a configuration change took place
    private boolean detailOnSavedInstance=false;
    private boolean alreadyCalledForDetails=false;

    private String spotId;
    private String spotLocation;

    private SpotDetails spotDetails=null;//object that holds all details about our spot

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        NetworkUtils.spotDetailFetchListener=this;

        bindViews();

        if(savedInstanceState==null){
            Intent intentThatStartedActivity = getIntent();
            if(intentThatStartedActivity==null){
                closeOnError("Passed empty Intent to the DetailActivity");
            }
            if(!checkIntentContent(intentThatStartedActivity)){
                closeOnError("The extras do not follow the specified format");
            }
            spotDetails=null;
        }else {
            checkViewsVisibility(savedInstanceState);
            checkSavedInstanceBundleContent(savedInstanceState);
        }
        Log.d(DetailActivity.DETAIL_TAG,"SpotId is: "+this.spotId+"| Location is: "+this.spotLocation);

        if(!alreadyCalledForDetails){
            if(spotDetails==null){
                if(NetworkUtils.isNetworkAvailable(this)){
                    alreadyCalledForDetails=true;
                    performSpotDetailRequest(spotId);
                }
            }else{
                Log.d(DetailActivity.DETAIL_TAG,"Another call is not necessary, details have already been received");
            }
        }
    }

    private void closeOnError(String errorMessage){
        finish();
        Log.d(DetailActivity.DETAIL_TAG,"Error: "+errorMessage);
    }

    private boolean checkIntentContent(Intent intent){
        Bundle extras= intent.getExtras();
        if(extras==null){
            return false;
        }
        spotLocation = extras.getString(MainActivity.SPOT_LOCATION_KEY_FOR_THE_DETAIL_ACTIVITY);
        spotId= extras.getString(MainActivity.SPOT_ID_KEY_FOR_THE_DETAIL_ACTIVITY);
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
        if(savedInstanceState.containsKey(SPOT_DETAILS_OBJECT)){
            spotDetails=(SpotDetails)savedInstanceState.getSerializable(SPOT_DETAILS_OBJECT);
        }else{
            spotDetails=null;
        }

        if(savedInstanceState.containsKey(SPOT_DETAIL_ID)){
            spotId=savedInstanceState.getString(SPOT_DETAIL_ID);
        }else{
            spotId=null;
            closeOnError("There is a problem getting the spotId");
        }

        if(savedInstanceState.containsKey(SPOT_DETAIL_LOCATION)){
            spotLocation=savedInstanceState.getString(SPOT_DETAIL_LOCATION);
        }else{
            spotLocation=null;
            closeOnError("There is a problem getting the spotLocation");
        }
    }

    private void bindViews(){
        detailProgressBar=(ProgressBar)findViewById(R.id.detail_progress_bar);
        detailNoConnectionTextView=(TextView)findViewById(R.id.detail_no_connection_text_view);
        detailProgressBar.setVisibility(View.INVISIBLE);
        detailNoConnectionTextView.setVisibility(View.INVISIBLE);
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

        outState.putSerializable(SPOT_DETAILS_OBJECT,spotDetails);

        outState.putString(SPOT_DETAIL_ID,spotId);

        outState.putString(SPOT_DETAIL_LOCATION,spotLocation);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSpotDetailFetcher(SpotDetails spotDetails) {
        this.alreadyCalledForDetails=false;
        this.spotDetails=spotDetails;
        if(spotDetails!=null){
            detailProgressBar.setVisibility(View.INVISIBLE);
            detailNoConnectionTextView.setVisibility(View.INVISIBLE);
            //Log.d(DetailActivity.DETAIL_TAG,"Details have been succesfully received");
            Toast.makeText(this,"Details have been succesfully received",Toast.LENGTH_SHORT).show();
        }else{
            detailProgressBar.setVisibility(View.INVISIBLE);
            detailNoConnectionTextView.setVisibility(View.INVISIBLE);
            Toast.makeText(this,"Details have not been succesfully received",Toast.LENGTH_SHORT).show();
        }
    }


}
