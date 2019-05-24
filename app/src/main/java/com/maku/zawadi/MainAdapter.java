package com.maku.zawadi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    ArrayList<String> mCategories;
    MapsActivity mapsActivity = new MapsActivity();

    public MainAdapter(ArrayList<String> mCategories) {
        this.mCategories = mCategories;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(mCategories.get(i));
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i){
                    case 0:
                        Intent intent1=new Intent(v.getContext(),MapsActivity.class);
                        intent1.putExtra("restaurant" , mCategories.get(i));
                        v.getContext().startActivity(intent1);
                        Log.i("MainAdapter", "onClick: " + mCategories.get(i));
                        Toast.makeText(v.getContext(), "Recycle Click " +mCategories.get(i) , Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Intent intent=new Intent(v.getContext(),RestaurantsActivity.class);
                        intent.putExtra("bar" , mCategories.get(i));
                        v.getContext().startActivity(intent);
                        Log.i("MainAdapter", "onClick: " + mCategories.get(i));
                        break;
                    case 2:
                        Log.i("MainAdapter", "onClick: " + mCategories.get(i));
                        break;

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.restaurants);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
