package com.maku.zawadi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    public String nom;
    public String prize;
    public String rname;

    EditText phoneNUmber;
    Button zawadi;

    //firebase instance variables
    private FirebaseDatabase mfirebaseDatabase; //connect to our db
    private DatabaseReference mMessagesDatabaseReference; //referencing specific part of db e.g messages

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

         final ArrayList<String> m = new ArrayList<>();

         sendToFirebase();


        // 1. get passed intent
        Intent intent = getIntent();

        // 2. get message value from intent
         nom = intent.getStringExtra("name");
         prize = intent.getStringExtra("price");
         rname = intent.getStringExtra("rname");


        // 3. show message on textView
        ((TextView)findViewById(R.id.tvname)).setText(nom);
        ((TextView)findViewById(R.id.tvprice)).setText(prize);
        ((TextView)findViewById(R.id.tvrest)).setText(rname);

        phoneNUmber = findViewById(R.id.numberEt);
        zawadi = findViewById(R.id.zawadi);
    }

    private void sendToFirebase() {

        zawadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CartActivity", "sendToFirebase: "  + "restaurant name " + rname + "food name " + nom + "phone numer " + phoneNUmber );
            }
        });

//        mfirebaseDatabase = FirebaseDatabase.getInstance();
//        mMessagesDatabaseReference = mfirebaseDatabase.getReference("zawadi-72069/tempCart");
//
//
//            String nome = (String) name.getText();
//            mMessagesDatabaseReference.push().child("food_item").setValue(nome);
//            mMessagesDatabaseReference.push().child("food_price").setValue(price.getText());
//            Log.w("firebase", "onClick: value "+ name.getText());
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
