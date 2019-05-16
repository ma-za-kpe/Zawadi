package com.maku.zawadi;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.mobile_number)TextView mmobile_numberTextView;
    @BindView(R.id.country_code)TextView mcountry_codeView;
    @BindView(R.id.appName)TextView mAppNameTextView;
    @BindView(R.id.restaurant)
    CardView mRestaurant;

    public static final String GOOGLE_ACCOUNT = "google_account";
     @BindView(R.id.profile_text) TextView profileName ;
    @BindView(R.id.profile_email) TextView profileEmail;
    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.sign_out) Button signOut;

    private GoogleSignInClient googleSignInClient;

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
        mcountry_codeView.setText( selected_country_code);
        mmobile_numberTextView.setText(mobile);

        mRestaurant.setOnClickListener(this);

        GoogleSignInAccount googleSignInAccount = getIntent().getParcelableExtra(GOOGLE_ACCOUNT);
        Picasso.get().load(googleSignInAccount.getPhotoUrl().toString()).centerInside().fit().into(profileImage);
        profileName.setText(googleSignInAccount.getDisplayName());
        profileEmail.setText(googleSignInAccount.getEmail());

    }

    private void setDataOnView() {

    }

    @Override
    public void onClick(View v) {

        if(v == mRestaurant) {

            Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
            startActivity(intent);
        } else if(v == signOut){

             /*
          Sign-out is initiated by simply calling the googleSignInClient.signOut API. We add a
          listener which will be invoked once the sign out is the successful
           */
            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //On Succesfull signout we navigate the user back to LoginActivity
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

        }

    }
}
