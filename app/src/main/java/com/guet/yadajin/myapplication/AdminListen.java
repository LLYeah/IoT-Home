package com.guet.yadajin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.guet.yadajin.*;

public class AdminListen implements  View.OnClickListener{
    Context context;
    EditText pid;
    EditText pwd;

    public AdminListen(Context context, EditText pid, EditText pwd){
        this.context=context;
        this.pid=pid;
        this.pwd=pwd;
    }


    public void onClick(View v) {//登陆按键转移
        if (pid.getText().toString().trim().equals("123456")==true&&pwd.getText().toString().trim().equals("123456")==true)
        {
            Intent intent = new Intent(context, TcpclientActivity.class);
            context.startActivity(intent);
            Toast.makeText(context, "登陆信息正确！", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "您无权限登入，请确认密码和账号", Toast.LENGTH_LONG).show();
        }
    }

}