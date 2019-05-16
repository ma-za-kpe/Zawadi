package com.maku.zawadi;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       runLauncher();

    }

    public void runLauncher() {

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start home activity
                startActivity(new Intent(Splash.this, LoginActivity.class));
                // close splash activity
                finish();
            }
        }, 5000);

    }
}
