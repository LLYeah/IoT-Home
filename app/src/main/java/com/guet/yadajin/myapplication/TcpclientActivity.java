package com.guet.yadajin.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.guet.yadajin.myapplication.fragments.MyViewPagerAdapter;

public class TcpclientActivity extends AppCompatActivity {

    public static final int PAGER_ONE =0;
    public static final int PAGER_TWO =1;

    private ViewPager viewPager;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        initView();
    }
    private void initView(){
        //配置viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        MyViewPagerAdapter myFragmentPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentPagerAdapter);

    }
}