package com.example.lego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.BufferUnderflowException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class welcome extends AppCompatActivity {
boolean islogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_welcome);
        final SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        final String account=sharedPreferences.getString("account", String.valueOf(11));
        final String password=sharedPreferences.getString("password",String.valueOf(11));
        islogin=sharedPreferences.getBoolean("islogin",false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Thread thread =new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            OkHttpClient client = new OkHttpClient();
                            Gson gson = new Gson();
                            user_login use = new user_login();
                            use.account=account;
                            use.password=password;
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody requestBody =RequestBody.create(JSON,gson.toJson(use));
                            Request request = new Request.Builder()
                                    .url("http://49.232.214.94/api/login")
                                    .post(requestBody)
                                    .build();
                            Response response = null;
                            response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            user user =gson.fromJson(responseData, com.example.lego.user.class);
                            com.example.lego.user.DataBean data = user.getData();
                            int code=user.getCode();
                            if(code==200)
                            {
                                String token=data.getToken();
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("token",token);
                                OkHttpClient client1 =new OkHttpClient();
                                Gson gson1=new Gson();
                                Request request1 = new Request.Builder()
                                        .addHeader("Authorization",token)
                                    .url("http://49.232.214.94/api/user")
                                    .build();
                                Response response1 = client1.newCall(request1).execute();
                                String responseData1 = response1.body().string();
                                information information =gson1.fromJson(responseData1, com.example.lego.information.class);
                                com.example.lego.information.DataBean data1=information.getData();
                                com.example.lego.information.DataBean.UserBean userData=data1.getUser();
                                String head=userData.getHead();
                                String myName = userData.getName();
                                boolean mySex = userData.isSex();
                                String myInfo = userData.getInfo();
                                editor.putString("head",head);
                                editor.putString("name",myName);
                                editor.putBoolean("sex",mySex);
                                editor.putString("info",myInfo);
                                editor.commit();
                            }
                        } catch (IOException e) {
                            islogin=false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(welcome.this,"网络未连接，请重新登陆",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }

                        //判断用户是否登录过
                        if (islogin) {  //如果登录过 跳转到 MainActivity

                            Intent intent=new Intent(welcome.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else { //如果没有登录过 跳转到 LoginActivity
                            Intent intent=new Intent(welcome.this,login.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                thread.start();


            }



        },1000);



    }
};
