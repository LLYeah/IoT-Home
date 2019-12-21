package com.guet.yadajin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserRegister extends AppCompatActivity {

    private String Register_userId;
    private String Register_password;
    private String Register_email;
    private String Register_phone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Button btn_regist = (Button)findViewById(R.id.btn_register);//    注册按钮
        btn_regist.setOnClickListener(new View.OnClickListener() {//        注册按钮监听
            @Override
            public void onClick(View v) {
                getWork();
            }
        });
    }
    protected void getWork(){
        EditText register_userId = (EditText) findViewById(R.id.register_userId);//    用户名获取
        EditText register_password = (EditText) findViewById(R.id.register_password);//    密码获取
        EditText register_email = (EditText) findViewById(R.id.register_email);//    邮箱获取
        EditText register_phone = (EditText) findViewById(R.id.register_phone);//    电话号码获取
        System.out.println("shusadh");

        Register_userId = register_userId.getText().toString();
        Register_password = register_password.getText().toString();
        Register_email = register_email.getText().toString();
        Register_phone = register_phone.getText().toString();

        new Thread(getRun).start();
    }
    Runnable getRun = new Runnable() {
        @Override
        public void run() {
            sendRequestWithOkHttp();
        }
    };

    private void sendRequestWithOkHttp(){
        Looper.prepare();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("userId",Register_userId);
            jsonObject.put("password",Register_password);
            jsonObject.put("email",Register_email);
            jsonObject.put("phoneNum",Register_phone);
        }catch (JSONException e){
            e.printStackTrace();
        }

        OkHttpClient client=new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));
        try{
            Request request= new Request.Builder()
                .url("http://192.168.0.47:8080/LoginPwd/SaveOne")
                .post(body)
                .build();
            Response response=client.newCall(request).execute();
            System.out.println(response);
            String responsedata  = response.body().string();
            if(responsedata.equals("false")){
                System.out.println("shibai");
                System.out.println(responsedata);
                Toast.makeText(getApplicationContext(), "注册失败！密码不可用，请重新输入密码。", Toast.LENGTH_LONG).show();
                Looper.loop();
            }else{
                System.out.println("chenggong");
                System.out.println(responsedata);
                Intent intent = new Intent(UserRegister.this,Login.class);
                UserRegister.this.startActivity(intent);
                Toast.makeText(getApplicationContext(), "注册成功！请登录。", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseJSONWithJSONObject(String jsonData){
        try{
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String register_userId = jsonObject.getString("\"userId\": ");
                String password = jsonObject.getString("\"password\": ");
                Log.d("UserRegister","userid is " + register_userId);
                Log.d("UserRegister","password is " + password);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
