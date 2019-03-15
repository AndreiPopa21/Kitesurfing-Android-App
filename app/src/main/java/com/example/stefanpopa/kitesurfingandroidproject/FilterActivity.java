package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FilterActivity extends AppCompatActivity {

    public static final String FILTER_TAG="FilterActivity";
    public static final String COUNTRY_EDIT_TEXT_KEY="country_edit_text_key";
    public static final String WIND_PROBABILITY_EDIT_TEXT_KEY="wind_prob_edit_text_key";

    public EditText countryEditText;
    public EditText windProbabilityEditText;
    public Button filterApplyButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
        setTitle("Filter");
        Intent intentThatStartedActivity = getIntent();
        if(intentThatStartedActivity==null){
            closeOnError(getString(R.string.null_intent_started_the_ativity_error_text));
        }
        bindViews();
    }

    private void bindViews(){
        countryEditText=(EditText)findViewById(R.id.filter_country_edit_text);
        windProbabilityEditText=(EditText)findViewById(R.id.filter_wind_probability_edit_text);
        filterApplyButton=(Button)findViewById(R.id.filter_apply_button);
        filterApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int windProbabilityValue;
                String countryText = countryEditText.getText().toString();
                if(windProbabilityEditText.getText().toString()==null ||
                        windProbabilityEditText.getText().toString().matches("")){
                    windProbabilityValue= 0;
                }else{
                    windProbabilityValue=Integer.valueOf(windProbabilityEditText.getText().toString());
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra(COUNTRY_EDIT_TEXT_KEY,countryText);
                resultIntent.putExtra(WIND_PROBABILITY_EDIT_TEXT_KEY,windProbabilityValue);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
    }

    private void closeOnError(String errorMessage){
        Log.d(FilterActivity.FILTER_TAG,"Error: "+errorMessage);
        finish();
    }
}
