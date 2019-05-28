package com.maku.zawadi;

import android.Manifest;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.maku.zawadi.POJOModels.Example;
import com.maku.zawadi.POJOModels.OpeningHours;
import com.maku.zawadi.POJOModels.Photo;
import com.maku.zawadi.POJOModels.Result;
import com.maku.zawadi.adapter.RestaurantListAdapter;
import com.maku.zawadi.model.Restaurant;
import com.maku.zawadi.networking.NearByApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    public static final String TAG = "MapsActivity";

    private double currentLatitude; //lat of user
    private double currentLongitude; //long of user

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mfusedLocationProviderClient;


    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 16.1f;
    private static final LatLngBounds LAT_LNG_BOUND = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 138)
    );
    //variables
    private Boolean mLocationPermissionGranted = false;

    //places
    private Button btnRestorentFind, btnHospitalBar;
    private LocationRequest mLocationRequest;
    private Location location;
    private int PROXIMITY_RADIUS = 8000;

    //arraylist
    ArrayList<Restaurant> newRestaurant;
    @BindView(R.id.re) RecyclerView recyclerView;
    RestaurantListAdapter mRestaurantListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: on create ...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);

//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnRestorentFind = (Button) findViewById(R.id.rest);
//        btnHospitalBar = (Button) findViewById(R.id.rest);
        //location permissions
        getLocationPermission();

    }

    //contains your lat and lon
    private void handleNewLocation(Location mlocation) {
        Log.d(TAG, mlocation.toString());

        currentLatitude = mlocation.getLatitude();
        currentLongitude = mlocation.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        Log.d(TAG, String.valueOf(latLng));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("You are here");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng), 11.0F));
    }


    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Log.d(TAG, "initMap: initialising map");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission() {

        Log.d(TAG, "getLocationPermission: getting location permission");

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }

                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted = true;
                    //initialize the map
                    initMap();
                }
            }
        }
    }


    private void getDeviceLOcation() {
        Log.d(TAG, "getDeviceLOcation: get the device current location");

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            if (mLocationPermissionGranted) {
                Task location = mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM, "current location" + currentLocation.getProvider());
                            }
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get the current location", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        } catch (SecurityException se) {
            Log.d(TAG, "getDeviceLOcation: SecurityException" + se.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: " + latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(markerOptions);
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient: ");
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    public void findPlaces(String placeType) {

        Log.d(TAG, "findPlaces: place..." + placeType);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        Log.d(TAG, "findPlaces: ..." + mLastLocation);

        try{


            String url = "https://maps.googleapis.com/maps/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Log.d(TAG, "findPlaces: URL..." + retrofit.toString());

            NearByApi service = retrofit.create(NearByApi.class);


            Call<Example> call = service.getNearbyPlaces(placeType, mLastLocation.getLatitude() + "," + mLastLocation.getLongitude(), PROXIMITY_RADIUS);

            call.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(Call<Example> call, final Response<Example> response) {
                    final ArrayList<Restaurant> restaurants = new ArrayList<>();

                    try {

                        mMap.clear();
                        // This loop will go through all the results and add marker on each location.
                        for (int i = 0; i < response.body().getResults().size(); i++) {
                            Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                            Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                            String placeName = response.body().getResults().get(i).getName();
                            String vicinity = response.body().getResults().get(i).getVicinity();
                            Integer priceLevel = response.body().getResults().get(i).getPriceLevel();
                            OpeningHours openingHours = response.body().getResults().get(i).getOpeningHours();
                            Double rating = response.body().getResults().get(i).getRating();
                            List<Photo> photUrls = response.body().getResults().get(i).getPhotos();
                            final List<String> photoUrl = new ArrayList<String>();

                            for (int y=0; y <photUrls.size(); y++){
                                photoUrl.add(photUrls.get(y).getHtmlAttributions().toString().substring(9, 74));
                                Log.d(TAG, "onResponse: photo" + photUrls.get(y).getHtmlAttributions().toString().substring(9, 74));
                            }

                            Restaurant restaurant = new Restaurant(placeName);
                            restaurants.add(restaurant);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {//update your views here
                                    Log.d(TAG, "run: updating text view");

                                    ArrayList<Result> rest = (ArrayList<Result>) response.body().getResults();
                                    mRestaurantListAdapter = new RestaurantListAdapter(rest);
                                    recyclerView.setAdapter(mRestaurantListAdapter);
                                    Log.d(TAG, "Number of restaurant received: " + rest.size());


                                }
                            });

                            Log.d(TAG, "findPlaces: name..." +  restaurant.getName());
                            Log.d(TAG, "findPlaces: rating..." +  rating);
                            Log.d(TAG, "findPlaces: placeName..." + placeName);
                            Log.d(TAG, "findPlaces: priceLevel..." + priceLevel);
//                            Log.d(TAG, "findPlaces: photoUrl..." + photoUrl);
//                            Log.d(TAG, "findPlaces: rating..." + rating);

                            MarkerOptions markerOptions = new MarkerOptions();
                            LatLng latLng = new LatLng(lat, lng);
                            // Location of Marker on Map
                            markerOptions.position(latLng);
                            // Title for Marker
                            markerOptions.title(placeName + " : " + vicinity);
                            // Color or drawable for marker
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            // add marker
                            Marker m = mMap.addMarker(markerOptions);
                            // move map camera
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
//                            Log.d(TAG, "onResponse: arraylist " + restaurants.get(i).getName());

                        }
                        display(restaurants);

                    } catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Example> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                    t.printStackTrace();
                    PROXIMITY_RADIUS += 10000;
                }
            });

        }
        catch (Exception e)
        {
            Log.d(TAG, "findPlaces: " + e.getMessage());
        }

    }

    public void display(ArrayList<Restaurant> al) {
        Log.d(TAG, "display: ");
        newRestaurant = new ArrayList<>();
        for (int i = 0; i < al.size(); i++) {
            Log.d(TAG, "display: arrayList" + al.get(i));
            newRestaurant.add(al.get(i));
        }
        Log.d(TAG, "display:New restaurant arrayList" + newRestaurant);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: oon start activity");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "map is ready", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        // Add a marker in Nairobi and move the camera

        if (mLocationPermissionGranted) {
            getDeviceLOcation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

        }

        btnRestorentFind.setOnClickListener(this);
//        LatLng nairobi = new LatLng(-1.28333, 36.81667);
//        mMap.addMarker(new MarkerOptions().position(nairobi).title("Marker in Nairobi"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(nairobi));
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: find restaurant");
        findPlaces("restaurant");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: onConnected...");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: Could not connect google api");
        Toast.makeText(this,"Could not connect google api",Toast.LENGTH_LONG).show();
    }

    protected void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates: startLocationUpdates");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: onLocationChanged");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(location!=null){
            this.location =  LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            Log.d(TAG, "onLocationChanged: " + latLng.latitude + " " + latLng.longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

            btnRestorentFind.setEnabled(true);
        }
    }

    /**********MENU***********/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRestaurantListAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                mRestaurantListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
