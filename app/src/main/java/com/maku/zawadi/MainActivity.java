package com.maku.zawadi;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.mobile_number)TextView mmobile_numberTextView;
    @BindView(R.id.country_code)TextView mcountry_codeView;
    @BindView(R.id.appName)TextView mAppNameTextView;
    @BindView(R.id.restaurant)TextView mRestaurant;

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
    }

    @Override
    public void onClick(View v) {

        if(v == mRestaurant) {

            Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
            startActivity(intent);
        }

    }
}
