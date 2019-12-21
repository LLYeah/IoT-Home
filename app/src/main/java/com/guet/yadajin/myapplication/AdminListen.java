package com.guet.yadajin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.guet.yadajin.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        new Thread(getRun).start();
    }

    Runnable getRun = new Runnable() {
        @Override
        public void run() {
            sendRequestWithOkhttp();
        }
    };


    private void sendRequestWithOkhttp(){
        Looper.prepare();
        String userid = pid.getText().toString().trim();
        String pass = pwd.getText().toString().trim();
        System.out.println("userid"+userid);
        System.out.println("pass"+ pass);
        MediaType JSON = MediaType.parse("application/json;charest=utf-8");
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("userId",userid);
            jsonObject.put("password",pass);
        }catch(JSONException e){
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody body  = RequestBody.create(JSON,String.valueOf(jsonObject));
        Request request = new Request.Builder()
                .url("http://192.168.0.47:8080/LoginPwd/FindPassword")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responsedata = response.body().string();
            if (responsedata.equals("false")) {
                Toast.makeText(context, "您无权限登入，请确认密码和账号", Toast.LENGTH_LONG).show();
                Looper.loop();
            } else {
                Intent intent = new Intent(context, TcpclientActivity.class);
                context.startActivity(intent);
                Toast.makeText(context, "登陆成功！", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}