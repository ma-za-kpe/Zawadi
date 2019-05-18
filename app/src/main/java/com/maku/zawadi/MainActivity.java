package com.maku.zawadi;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
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
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public  static  final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.restaurant)
    CardView mRestaurant;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;

    public static final String GOOGLE_ACCOUNT = "google_account";
     @BindView(R.id.profile_text) TextView profileName ;
    @BindView(R.id.profile_email) TextView profileEmail;
    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.sign_out) Button signOut;

    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_main) Toolbar toolbar;


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

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String photo = intent.getStringExtra("photo");
        String name = intent.getStringExtra("name");
        profileName.setText(name);
        profileEmail.setText(email);
        Log .d(TAG, "Test user photo " + photo);

        Picasso.get()
                .load(photo)
                .into(profileImage);

        mRestaurant.setOnClickListener(this);

//        GoogleSignInAccount googleSignInAccount = getIntent().getParcelableExtra(GOOGLE_ACCOUNT);
//        Picasso.get().load(googleSignInAccount.getPhotoUrl().toString()).centerInside().fit().into(profileImage);
//        profileName.setText(googleSignInAccount.getDisplayName());
//        profileEmail.setText(googleSignInAccount.getEmail());


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

        signOut.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v == mRestaurant) {

            Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
            startActivity(intent);
        } else if(v == signOut){

            UserSignOutFunction();
        }

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

}
