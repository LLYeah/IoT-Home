package com.guet.yadajin.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class AttendListen implements  View.OnClickListener{
    Activity context;

    public AttendListen(Activity context){
        this.context=context;
    }

    @Override
    public void onClick(View v) {
         Intent intent=new Intent(context,UserRegister.class);//注册按键转移
         context.startActivity(intent);
         context.finish();
    }

}
