package com.maku.zawadi.networking;

import com.maku.zawadi.model.NearByApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearByApi {

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyBxn7Mc7qHoABw-HePZYUFTklINIGFKtis") //used to call server corresponding to url
    Call<NearByApiResponse> getNearbyPlaces(@Query("type") String type,
                                            @Query("location") String location,
                                            @Query("radius") int radius);
}



