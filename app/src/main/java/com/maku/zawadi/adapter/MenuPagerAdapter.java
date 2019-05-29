package com.maku.zawadi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maku.zawadi.MenuFragment;
import com.maku.zawadi.POJOModels.Result;

import java.util.ArrayList;

public class MenuPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Result> mResults;

    public MenuPagerAdapter(FragmentManager fm, ArrayList<Result> results) {
        super(fm);
        mResults = results;
    }

    @Override
    public Fragment getItem(int i) {
        return MenuFragment.newInstance(mResults.get(i));
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mResults.get(position).getName();
    }
}
