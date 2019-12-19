package com.guet.yadajin.myapplication.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


public class MyViewPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 2;
    private OneFragment oneFragment;
    private TwoFragment twoFragment;

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
        oneFragment = new OneFragment();
        twoFragment = new TwoFragment();
    }

    @Override
    public Fragment getItem(int i) {  //获取当前要显示的view
        Fragment fragment = null;
        switch (i) {
            case TestActivity.PAGER_ONE:
                fragment = oneFragment;
                break;
            case TestActivity.PAGER_TWO:
                fragment = twoFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {  //获得viewPager中有多少个view
        return PAGER_COUNT;
    }
}
