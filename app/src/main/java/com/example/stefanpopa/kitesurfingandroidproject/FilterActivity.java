package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class FilterActivity extends AppCompatActivity {

    public static final String FILTER_TAG="FilterActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
        setTitle("Filter");
        Intent intentThatStartedActivity = getIntent();
        if(intentThatStartedActivity==null){
            closeOnError("Null intent started the activity");
        }

    }

    private void closeOnError(String errorMessage){
        Log.d(FilterActivity.FILTER_TAG,"Error: "+errorMessage);
        finish();
    }
}
