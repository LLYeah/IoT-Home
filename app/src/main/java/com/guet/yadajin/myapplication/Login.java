package com.guet.yadajin.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private Button login;
    private Button register;
    private EditText Pid;
    private EditText Passwd;
    private String pid;
    private String passwd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Pid = (EditText) findViewById(R.id.UserName);//用户名输入
        Passwd = (EditText) findViewById(R.id.UserPassword);//用户密码输入
        login = (Button) findViewById(R.id.bt_login_submit);//登陆按键
        register = (Button) findViewById(R.id.bt_login_register);//注册按键

        pid = Pid.getText().toString();//获得用户名
        passwd = Passwd.getText().toString();//获得登陆密码

        AttendListen attendance = new AttendListen(this);
        register.setOnClickListener(attendance);
        AdminListen admin = new AdminListen(this, Pid,Passwd);
        login.setOnClickListener(admin);
    }
}
