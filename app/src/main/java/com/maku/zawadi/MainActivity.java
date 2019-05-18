package com.maku.zawadi;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
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

    @BindView(R.id.mobile_number)TextView mmobile_numberTextView;
    @BindView(R.id.country_code)TextView mcountry_codeView;
    @BindView(R.id.appName)TextView mAppNameTextView;
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
    @BindView(R.id.editTextCode) EditText editTextCode;
    @BindView(R.id.btnVerify) Button btnVerify;

    private GoogleSignInClient googleSignInClient;
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Typeface kushanScriptRegular = Typeface.createFromAsset(getAssets(), "fonts/kaushanscriptregular.otf");
        mAppNameTextView.setTypeface(kushanScriptRegular);

        Intent intent = getIntent();
        String selected_country_code = intent.getStringExtra("selected_country_code");
        String mobile = intent.getStringExtra("mobile");
        String email = intent.getStringExtra("email");
        String photo = intent.getStringExtra("photo");
        String name = intent.getStringExtra("name");
        mcountry_codeView.setText( selected_country_code);
        mmobile_numberTextView.setText(mobile);
        profileName.setText(name);
        profileEmail.setText(email);
        String fullNumber = selected_country_code+mobile;
        Log .d(TAG, "Test user mobile " + fullNumber);

        Log .d(TAG, "Test user photo " + photo);

        Picasso.get()
                .load(photo)
                .into(profileImage);

        mRestaurant.setOnClickListener(this);
        btnVerify.setOnClickListener(this);

        //if the automatic sms detection worked,
        sendVerificationCode(fullNumber);

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
        } else if(v == btnVerify){
            //if the automatic sms detection did not work, user can also enter the code manually
            //so adding a click listener to the button
            String code = editTextCode.getText().toString().trim();
            if (code.isEmpty() || code.length() < 6) {
                editTextCode.setError("Enter valid code");
                editTextCode.requestFocus();
                return;
            }

            //verifying the code entered manually
            verifyVerificationCode(code);

        }

    }

    //firebase phone authentication



    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
//                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
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
}
