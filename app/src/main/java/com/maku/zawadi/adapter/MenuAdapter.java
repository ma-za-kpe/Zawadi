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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maku.zawadi.CartActivity;
import com.maku.zawadi.MenuActivity;
import com.maku.zawadi.MenuFragment;
import com.maku.zawadi.R;
import com.maku.zawadi.constants.Constants;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.RestaurantViewHolder> {

    private ArrayList<String> mMenu;
    OnTextClickListener listener;

    //firebase instance variables
    private FirebaseDatabase mfirebaseDatabase; //connect to our db
    private DatabaseReference mMessagesDatabaseReference; //referencing specific part of db e.g messages

    public MenuAdapter(ArrayList<String> mMenu, OnTextClickListener listener) {
        this.mMenu = mMenu;
        this.listener = listener;
    }

    private SharedPreferences mSharedPreferences;
    private String mName;
    private String mRating;

    @NonNull
    @Override
    public MenuAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menurow, viewGroup, false);
        MenuAdapter.RestaurantViewHolder viewHolder = new MenuAdapter.RestaurantViewHolder(view);
        return viewHolder;
    }

    String nom;

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.RestaurantViewHolder restaurantViewHolder, int i) {
       nom = mMenu.get(i);
        restaurantViewHolder.name.setText(nom);
    }

    @Override
    public int getItemCount() {
        return mMenu.size();
    }

    public interface OnTextClickListener {
        void onTextClick(String name, String price);
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView name;
        public TextView price;
        public CardView cardView;


        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.foodName);
            price = itemView.findViewById(R.id.price);
            cardView = itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);


        }
        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();

            String data = (String) name.getText();
            String data1 = (String) price.getText();
            listener.onTextClick(data, data1);

        }
    }
}
