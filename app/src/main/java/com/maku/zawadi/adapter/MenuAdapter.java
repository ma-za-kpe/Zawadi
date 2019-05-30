package com.maku.zawadi.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maku.zawadi.MenuActivity;
import com.maku.zawadi.R;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.RestaurantViewHolder> {

    private ArrayList<String> mMenu;

    public MenuAdapter(ArrayList<String> mMenu) {
        this.mMenu = mMenu;
    }

    @NonNull
    @Override
    public MenuAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menurow, viewGroup, false);
        MenuAdapter.RestaurantViewHolder viewHolder = new MenuAdapter.RestaurantViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.RestaurantViewHolder restaurantViewHolder, int i) {
        restaurantViewHolder.name.setText(mMenu.get(i));
    }

    @Override
    public int getItemCount() {
        return mMenu.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView name;
        public CardView cardView;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.foodName);
            cardView = itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(v.getContext(), MenuActivity.class);
            if (intent != null)
            {
                intent.putExtra("position", itemPosition);
                v.getContext().startActivity(intent);
            }
        }
    }
}
