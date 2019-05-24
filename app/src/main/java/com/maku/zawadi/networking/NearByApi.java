package com.maku.zawadi.networking;

import com.maku.zawadi.POJOModels.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearByApi {

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyBxn7Mc7qHoABw-HePZYUFTklINIGFKtis")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);



    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-1.28333,36.81667&radius=1500&type=supermarket&key=AIzaSyBxn7Mc7qHoABw-HePZYUFTklINIGFKtis
}



