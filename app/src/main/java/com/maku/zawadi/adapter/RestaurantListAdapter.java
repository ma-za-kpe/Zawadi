package com.maku.zawadi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maku.zawadi.POJOModels.Result;
import com.maku.zawadi.R;

import java.util.List;
import java.util.Locale;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {
    private List<Result> mRestaurants;
    private Context mContext;
    private int rowLayout;

    public RestaurantListAdapter(List<Result> mRestaurants) {
        this.mRestaurants = mRestaurants;
        this.mContext = mContext;
        this.rowLayout = rowLayout;
    }


    @NonNull
    @Override
    public RestaurantListAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item, parent, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListAdapter.RestaurantViewHolder restaurantViewHolder, int i) {
        restaurantViewHolder.mNameTextView.setText(mRestaurants.get(i).getName());
        restaurantViewHolder.mRatingTextView.setText(String.format(Locale.getDefault(), "%f", mRestaurants.get(i).getRating()));
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTextView;
        TextView mRatingTextView;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.restaurantNameTextView);
            mRatingTextView = itemView.findViewById(R.id.restaurantRatingTextView);
        }
    }

}
