package com.maku.zawadi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    TextView mPasswordTextView;

    TextView mEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmailTextView = (TextView) findViewById(R.id.email);
        mPasswordTextView = (TextView) findViewById(R.id.pwd);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        mEmailTextView.setText("email: " + email);
        mPasswordTextView.setText("password: " + password);
    }
}
