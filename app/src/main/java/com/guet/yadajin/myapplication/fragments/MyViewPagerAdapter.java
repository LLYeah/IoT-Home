package com.guet.yadajin.myapplication.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Pager适配器
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {
//    private final int PAGER_COUNT = 2;
//    private OneFragment oneFragment;
//    private TwoFragment twoFragment;
    private Fragment[] fragments = new Fragment[] {new OneFragment(), new TwoFragment()};

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
//        oneFragment = new OneFragment();
//        twoFragment = new TwoFragment();
    }

    /**
     * 获取当前要显示的view
     * @param i
     * @return
     */
    @Override
    public Fragment getItem(int i) {
        return fragments[i];
//        Fragment fragment = null;
//        switch (i) {
//            case TestActivity.PAGER_ONE:
//                fragment = oneFragment;
//                break;
//            case TestActivity.PAGER_TWO:
//                fragment = twoFragment;
//                break;
//        }
//        return fragment;
    }

    /**
     * 获得viewPager中有多少个view
     * @return
     */
    @Override
    public int getCount() {
        return fragments.length;
    }
}
