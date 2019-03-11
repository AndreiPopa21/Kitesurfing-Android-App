package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_TAG="DetailActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Intent intentThatStartedActivity = getIntent();
        if(intentThatStartedActivity==null){
            closeOnError("Passed empty Intent to the DetailActivity");
        }
        if(!checkIntentContent(intentThatStartedActivity)){
            closeOnError("The extras do not follow the specified format");
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
}
