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
    @BindView(R.id.et_fragment_register_step_one_mobile)
    TextView mMobile;
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

        }

    }
}
