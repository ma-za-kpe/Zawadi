package com.maku.zawadi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.maku.zawadi.POJOModels.Result;
import com.maku.zawadi.R;

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
                filteredList.add((Result) mRestaurantsSearch);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Result result : mRestaurantsSearch) {
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
            mRestaurants.add((Result) results.values);
            notifyDataSetChanged();
        }
    };
}
