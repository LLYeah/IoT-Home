package com.guet.yadajin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class AttendListen implements  View.OnClickListener{
    Context context;

    public AttendListen(Context context){
        this.context=context;
    }

    @Override
    public void onClick(View v) {
         Intent intent=new Intent(context,TcpclientActivity.class);//注册按键转移
         context.startActivity(intent);
    }
}
