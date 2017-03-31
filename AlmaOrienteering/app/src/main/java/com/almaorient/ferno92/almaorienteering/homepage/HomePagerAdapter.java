package com.almaorient.ferno92.almaorienteering.homepage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lucas on 28/03/2017.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {
    private int mNumItems;
    private final FragmentManager mFragmentManager;
    private NewMainActivity mMainActivity;

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
        this.mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return HomeElementFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mNumItems;
    }

    public void setNumItems(int num){
        mNumItems = num;
    }
}
