package com.maku.zawadi;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public  static  final String TAG = LoginActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    @BindView(R.id.loginButton) Button mLoginButton;
    @BindView(R.id.wolcomeTextView)
    TextView mwolcomeTextView;
    @BindView(R.id.emailTextView)
    TextView memailTextView;
    @BindView(R.id.et_fragment_register_step_one_mobile)
    TextView mMobile;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;

    @BindView(R.id.sign_in_button)
     SignInButton googleSignInButton;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        mLoginButton.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d(TAG, "Not logged in");
        }
    }

    @Override
    public void onClick(View v) {

        if(v == mLoginButton) {

            String selected_country_code = ccp.getSelectedCountryCodeWithPlus();
            String mobile = mMobile.getText().toString();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("selected_country_code", selected_country_code);
            intent.putExtra("mobile", mobile);
            startActivity(intent);
        }else if (v == ccp){

            ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
                @Override
                public void onCountrySelected() {
                    //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                    String selected_country_code = ccp.getSelectedCountryCodeWithPlus();
                }
            });

        } else if(v == googleSignInButton){
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 101);
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d(TAG, account.getEmail());
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }
    }


    private void onLoggedIn(GoogleSignInAccount account) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.GOOGLE_ACCOUNT, account);
        startActivity(intent);
    }
}
