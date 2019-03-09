package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Result_Children;

import retrofit2.Response;

public class SpotsAdapter extends RecyclerView.Adapter<SpotsAdapter.SpotsViewHolder> {

    private Response<Spot_All_Result> spotsResponse;
    private Context context;
    private LayoutInflater mInflater;

    public SpotsAdapter(Context context, Response<Spot_All_Result> spotsResponse) {
        this.spotsResponse=spotsResponse;
        this.context=context;
        this.mInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SpotsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.spot_item,viewGroup,false);
        SpotsViewHolder spotsViewHolder = new SpotsViewHolder(view);
        return spotsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpotsViewHolder spotsViewHolder, int i) {
        Spot_All_Result_Children info = this.spotsResponse.body().getAll_result_children().get(i);
        spotsViewHolder.setId(info.getId());
        spotsViewHolder.setName(info.getName());
        spotsViewHolder.setCountry(info.getCountry());
        spotsViewHolder.setWhenToGo(info.getWhenToGo());
        spotsViewHolder.setFavorite(info.isFavorite());

        spotsViewHolder.getNameTextView().setText(info.getName());
        spotsViewHolder.getCountryTextView().setText(info.getCountry());
    }

    @Override
    public int getItemCount() {
        if(spotsResponse==null){
            return 0;
        }else{
            return spotsResponse.body().getAll_result_children().size();
        }
    }

    public class SpotsViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView countryTextView;

        public TextView getNameTextView() {
            return nameTextView;
        }

        public TextView getCountryTextView() {
            return countryTextView;
        }

        private String id;
        private String name;
        private String country;
        private String whenToGo;
        private boolean isFavorite;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public SpotsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView=(TextView)itemView.findViewById(R.id.region_text_view);
            countryTextView=(TextView)itemView.findViewById(R.id.country_text_view);
        }
    }
}