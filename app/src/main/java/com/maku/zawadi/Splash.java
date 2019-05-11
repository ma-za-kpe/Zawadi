package com.maku.zawadi;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Start home activity
        startActivity(new Intent(Splash.this, LoginActivity.class));
        // close splash activity
        finish();

    }
}
