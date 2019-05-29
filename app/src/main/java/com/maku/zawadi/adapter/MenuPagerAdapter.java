package com.maku.zawadi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maku.zawadi.MenuDetailFragment;
import com.maku.zawadi.POJOModels.Result;

import java.util.ArrayList;

public class MenuPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Result> mResult;

    public MenuPagerAdapter(FragmentManager fm, ArrayList<Result> results) {
        super(fm);

        mResult = results;
    }

    @Override
    public Fragment getItem(int i) {
        return MenuDetailFragment.newInstance(mResult.get(i));
    }

    @Override
    public int getCount() {
        return mResult.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mResult.get(position).getName();
    }

}
