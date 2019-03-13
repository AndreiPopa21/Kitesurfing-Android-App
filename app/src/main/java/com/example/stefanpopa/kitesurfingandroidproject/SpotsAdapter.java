package com.example.stefanpopa.kitesurfingandroidproject;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Result;
import com.example.stefanpopa.kitesurfingandroidproject.api_spot_get_all_models.Spot_All_Result_Children;

import retrofit2.Response;

public class SpotsAdapter extends RecyclerView.Adapter<SpotsAdapter.SpotsViewHolder> {

    public interface SpotItemClickListener{
        void onSpotClick(String spotId,String location);
    }
    public interface FavoriteStarClickListener{
        void onFavoriteStarClick(SpotsViewHolder itemView);
    }


    private Spot_All_Result spotsList;
    private Context context;
    private LayoutInflater mInflater;
    private SpotItemClickListener spotItemClickListener;
    private FavoriteStarClickListener favoriteStarClickListener;

    public SpotsAdapter(Context context,
                        Spot_All_Result spotsList,
                        SpotItemClickListener spotItemClickListener,
                        FavoriteStarClickListener favoriteStarClickListener) {
        this.spotsList=spotsList;
        this.context=context;
        this.mInflater=LayoutInflater.from(context);
        this.spotItemClickListener=spotItemClickListener;
        this.favoriteStarClickListener=favoriteStarClickListener;
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
        Spot_All_Result_Children info = this.spotsList.getAll_result_children().get(i);
        spotsViewHolder.setId(info.getId());
        spotsViewHolder.setName(info.getName());
        spotsViewHolder.setCountry(info.getCountry());
        spotsViewHolder.setWhenToGo(info.getWhenToGo());
        spotsViewHolder.setFavorite(info.isFavorite());

        spotsViewHolder.setIndex_in_list(i);

        spotsViewHolder.getNameTextView().setText(info.getName());
        spotsViewHolder.getCountryTextView().setText(info.getCountry());
        if(info.isFavorite()){
           spotsViewHolder.getFavoriteButton().setBackground(ContextCompat.getDrawable(this.context,
                   R.drawable.star_on));
        }else{
            spotsViewHolder.getFavoriteButton().setBackground(ContextCompat.getDrawable(this.context,
                    R.drawable.star_off));
        }
    }

    @Override
    public int getItemCount() {
        if(spotsList==null){
            return 0;
        }else{
            return spotsList.getAll_result_children().size();
        }
    }

    public class SpotsViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView countryTextView;
        private Button favoriteButton;

        public Button getFavoriteButton() {
            return favoriteButton;
        }

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

        private int index_in_list;

        public int getIndex_in_list() {
            return index_in_list;
        }

        public void setIndex_in_list(int index_in_list) {
            this.index_in_list = index_in_list;
        }

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
            nameTextView=(TextView)itemView.findViewById(R.id.name_text_view);
            countryTextView=(TextView)itemView.findViewById(R.id.country_text_view);
            favoriteButton=(Button)itemView.findViewById(R.id.favorite_star_button);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spotItemClickListener.onSpotClick(getId(),getName());
                }
            });
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    favoriteStarClickListener.onFavoriteStarClick(SpotsViewHolder.this);
                }
            });
        }
    }
}
