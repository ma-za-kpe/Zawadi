package com.maku.zawadi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // 1. get passed intent
        Intent intent = getIntent();

        // 2. get message value from intent
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String rname = intent.getStringExtra("rname");

        //converting hashed strings back to normal strings

        // 3. show message on textView
        ((TextView)findViewById(R.id.tvname)).setText(name);
        ((TextView)findViewById(R.id.tvprice)).setText(rname);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
