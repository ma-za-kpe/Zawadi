package com.maku.zawadi;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.maku.zawadi.constants.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    public  static  final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences mSharedPreferences;
    private String mEmail;
    private String mPhoto;
    private String mPhoneNumber;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;

    public static final String GOOGLE_ACCOUNT = "google_account";

    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_main) Toolbar toolbar;

    //Arraylist of categories
    ArrayList<String> mCategories;
    @BindView(R.id.catRecycler) RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    public static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Typeface kushanScriptRegular = Typeface.createFromAsset(getAssets(), "fonts/kaushanscriptregular.otf");

        //ArrayList
        mCategories = new ArrayList<String>();
        mCategories.add("CAFE");
        mCategories.add("LIQUOR_STORE");
        mCategories.add("RESTAURANT");
        mCategories.add("SUPERMARKET");
        mCategories.add("PHARMACY");
        mCategories.add("NIGHT_CLUB");



        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new MainAdapter(mCategories);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        // GoogleSignInOptions 개체 구성
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log .d(TAG, "Login fail");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // initialize auth
        mAuth = FirebaseAuth.getInstance();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEmail = mSharedPreferences.getString(Constants.PREFERENCES_EMAIL_KEY, null);
        mPhoto = mSharedPreferences.getString(Constants.PREFERENCES_PHOTO_KEY, null);
        mPhoneNumber = mSharedPreferences.getString(Constants.PREFERENCES_ID_PHONE_NUMBER, null);

//        Toast.makeText(MainActivity.this, "number " + mPhoneNumber,
//                Toast.LENGTH_SHORT).show();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        //email
        TextView Email = (TextView) header.findViewById(R.id.nav_header_textView);
        Email.setText("Email " + mEmail);

        //photo
        ImageView imageView = (ImageView) header.findViewById(R.id.nav_header_imageView);
        Picasso.get()
                .load(mPhoto)
                .into(imageView);

        //number
        TextView Number = (TextView) header.findViewById(R.id.nav_header_phone);
        Number.setText("Phone Number " + mPhoneNumber);

        setNavigationViewListener();

    }

    //check if google services are ok and enabled
    public boolean isServiceOk(){
        Log.d(TAG, "checking google play service version?");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, " google play service is working");
            return true;
        } else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //a fixable error occured
            Log.d(TAG, "an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this,"you cannot make request", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onClick(View v) {

//        if(v == mRestaurant) {
//
//            Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
//            startActivity(intent);
//        }

    }

    //google firebase signin

    public void UserSignOutFunction() {

        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

            @Override
            public void onConnected(@Nullable Bundle bundle) {

                mAuth.signOut();
                if (mGoogleApiClient.isConnected()) {

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(@NonNull Status status) {

                            if (status.isSuccess()) {

                                Log.d(TAG, "onResult: ");
                                Log.d(TAG, "User Logged out");
//                                setResult(ResultCode.SIGN_OUT_SUCCESS);

                            } else {

//                                setResult(ResultCode.SIGN_OUT_FAIL);
                            }

                            finish();
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

                Log.d(TAG, "onConnectionSuspended: ");

//                setResult(ResultCode.SIGN_OUT_FAIL);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        switch (menuItem.getItemId()) {

            case R.id.nav_item_one : {
                Toast.makeText(MainActivity.this, "ALL VENDORS",
                        Toast.LENGTH_SHORT).show();
                break;
            }case R.id.nav_item_two : {
                Toast.makeText(MainActivity.this, "YOU HAVE SELECTED GIFTS",
                        Toast.LENGTH_SHORT).show();
                break;
            }case R.id.nav_item_three : {
                Toast.makeText(MainActivity.this, "YOU HAVE SELECTED CART",
                        Toast.LENGTH_SHORT).show();
                break;
            }case R.id.nav_item_four : {
                Toast.makeText(MainActivity.this,  mPhoneNumber,
                        Toast.LENGTH_SHORT).show();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 0715434451));
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(phoneIntent);
                UserSignOutFunction();
                break;
            } case R.id.nav_item_five : {
                UserSignOutFunction();
                break;
            } case R.id.nav_item_SIX : {
                if(isServiceOk()){
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
                break;
            } default:
                return super.onOptionsItemSelected(menuItem);
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //method to set Listener
    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}
