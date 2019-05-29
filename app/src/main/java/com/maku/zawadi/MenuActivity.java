package com.maku.zawadi;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maku.zawadi.POJOModels.Result;
import com.maku.zawadi.adapter.MenuPagerAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;

public class MenuActivity extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private MenuPagerAdapter adapterViewPager;
    ArrayList<Result> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mViewPager = findViewById(R.id.viewPager);

        mRestaurants = Parcels.unwrap(getIntent().getParcelableExtra("restaurants"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        adapterViewPager = new MenuPagerAdapter(getSupportFragmentManager(), mRestaurants);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
