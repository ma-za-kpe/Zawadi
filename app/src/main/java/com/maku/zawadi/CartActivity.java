package com.maku.zawadi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

        sendToFirebase();

    }

    private void sendToFirebase() {

        zawadi.setOnTouchListener(new OnSwipeTouchListener(CartActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(CartActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {

                overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);

                // Generate random id, for example 283952-V8M32
                char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
                Random rnd = new Random();
                StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)) + "-");
                for (int i = 0; i < 5; i++)
                    sb.append(chars[rnd.nextInt(chars.length)]);

                String code =  sb.toString();

                int phoneNumber = Integer.parseInt(phoneNUmber.getText().toString());
                Log.d("CartActivity", "sendToFirebase: "  + "restaurant name " + rname + "food name " + nom + "phone numer " + phoneNumber);

                //save to firebase
                mfirebaseDatabase = FirebaseDatabase.getInstance();
                mMessagesDatabaseReference = mfirebaseDatabase.getReference("zawadi-72069/cart");

                String key = mMessagesDatabaseReference.push().getKey(); // this will create a new unique key
                Map<String, Object> value = new HashMap<>();
                value.put("restaurant name", rname);
                value.put("food name", nom);
                value.put("phone number", String.valueOf(phoneNumber));
                value.put("timestamp", ServerValue.TIMESTAMP);
                mMessagesDatabaseReference.child(key).child("order").setValue(value);

                sendSMS(phoneNumber, code);
                Toast.makeText(CartActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(CartActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(CartActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void sendSMS(int phoneNumber, String code) {

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(String.valueOf(phoneNumber), null, "Go with this code "+ code + " to " + rname +" and redeem your item. Download " + getString(R.string.app_name).toLowerCase() + " app from playstore to get SHARING", null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
