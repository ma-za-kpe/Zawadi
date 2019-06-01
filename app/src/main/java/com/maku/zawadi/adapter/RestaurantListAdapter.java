package com.maku.zawadi.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.maku.zawadi.MenuActivity;
import com.maku.zawadi.MenuFragment;
import com.maku.zawadi.POJOModels.Result;
import com.maku.zawadi.R;
import com.maku.zawadi.constants.Constants;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> implements Filterable {
    private List<Result> mRestaurants;
    private List<Result> mRestaurantsSearch;
    private Context mContext;
    private int rowLayout;


    public RestaurantListAdapter(List<Result> mRestaurants) {
        this.mRestaurants = mRestaurants;
        mRestaurantsSearch = new ArrayList<Result>();
        this.mContext = mContext;
        this.rowLayout = rowLayout;
    }

    public RestaurantListAdapter() {

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
        final Result name = mRestaurants.get(i);
        final Result rating = mRestaurants.get(i);
        restaurantViewHolder.mNameTextView.setText(name.getName());
        restaurantViewHolder.mRatingTextView.setText(String.format(Locale.getDefault(), "%f", rating.getRating()));

    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder  implements  View.OnClickListener{

        TextView mNameTextView;
        TextView mRatingTextView;
        CardView mCardView;
        Button cartBtn;


        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.restaurantNameTextView);
            mRatingTextView = itemView.findViewById(R.id.restaurantRatingTextView);
            mCardView = itemView.findViewById(R.id.card_view);
            cartBtn = itemView.findViewById(R.id.cartBtn);


            mCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == mCardView){
                int itemPosition = getLayoutPosition();
                Intent intent = new Intent(view.getContext(), MenuActivity.class);
                if (intent != null)
                {
                    intent.putExtra("position", itemPosition);
                    intent.putExtra("restaurants", Parcels.wrap(mRestaurants));
                    view.getContext().startActivity(intent);
                }
            } else if(view == cartBtn) {

            }
        }
    }

    /*************************** SERACH ISSUES*********************/

    @Override
    public Filter getFilter() {
        Log.d("RestaurantListAdapter", "getFilter: ");
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Result>  filteredList =  new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.add((Result) mRestaurants );
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Result result : mRestaurants) {
                    if (result.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(result);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults ;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mRestaurants.clear();
            mRestaurants.addAll((List<Result>) results.values);
            notifyDataSetChanged();
        }
    };
}
