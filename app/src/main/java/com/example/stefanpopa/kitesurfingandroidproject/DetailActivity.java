package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_TAG="DetailActivity";
    public static final String IS_DETAIL_NO_CONNECTION_TEXT_VIEW_VISIBLE="is_detail_no_connection_text_view_visible";
    public static final String IS_DETAIL_PROGRESS_BAR_VISIBLE="is_detail_progress_bar_visible";
    public static final String DETAIL_ON_SAVED_INSTANCE="detail_on_saved_instance";

    private ProgressBar detailProgressBar;
    private TextView detailNoConnectionTextView;

    //boolean that tells us whether a configuration change took place
    private boolean detailOnSavedInstance=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        bindViews();

        if(savedInstanceState==null){
            Intent intentThatStartedActivity = getIntent();
            if(intentThatStartedActivity==null){
                closeOnError("Passed empty Intent to the DetailActivity");
            }
            if(!checkIntentContent(intentThatStartedActivity)){
                closeOnError("The extras do not follow the specified format");
            }
        }else {
            checkViewsVisibility(savedInstanceState);
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
        String location = extras.getString(MainActivity.SPOT_LOCATION_KEY_FOR_THE_DETAIL_ACTIVITY);
        String ceva =extras.getString("MANCAMI-A");
        String spotId= extras.getString(MainActivity.SPOT_ID_KEY_FOR_THE_DETAIL_ACTIVITY);
        if(location==null || spotId==null){
            return false;
        }
        return true;
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

        super.onSaveInstanceState(outState);
    }

    public class SpotDetails{

        public String spotId;
        public String name;
        private double longitude;
        private double latitude;
        private int windProbability;
        private String country;
        private String whenToGo;
        private boolean isFavorite;

        public SpotDetails(String spotId,
                           String name,
                           double longitude,
                           double latitude,
                           int windProbability,
                           String country,
                           String whenToGo,
                           boolean isFavorite) {
            this.spotId = spotId;
            this.name = name;
            this.longitude = longitude;
            this.latitude = latitude;
            this.windProbability = windProbability;
            this.country = country;
            this.whenToGo = whenToGo;
            this.isFavorite = isFavorite;
        }

        public String getSpotId() {
            return spotId;
        }

        public void setSpotId(String spotId) {
            this.spotId = spotId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public int getWindProbability() {
            return windProbability;
        }

        public void setWindProbability(int windProbability) {
            this.windProbability = windProbability;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getWhenToGo() {
            return whenToGo;
        }

        public void setWhenToGo(String whenToGo) {
            this.whenToGo = whenToGo;
        }

        public boolean isFavorite() {
            return isFavorite;
        }

        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
        }
    }
}
