package com.maku.zawadi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.loginButton) Button mLoginButton;
    @BindView(R.id.wolcomeTextView)
    TextView mwolcomeTextView;
    @BindView(R.id.emailTextView)
    TextView memailTextView;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mLoginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == mLoginButton) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            startActivity(intent);
        }else if (v == ccp){

            ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
                @Override
                public void onCountrySelected() {
                    //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                    String selected_country_code = ccp.getSelectedCountryCodeWithPlus();
                }
            });

        }

    }
}
