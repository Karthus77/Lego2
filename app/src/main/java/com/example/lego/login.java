package com.example.lego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
class user_login{
    String account;
    String password;
}

public class login extends AppCompatActivity {
    EditText login_account;
    EditText login_password;
    Button login_login;
    TextView zhuce;
    String msg;
    int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        login_account =findViewById(R.id.account);
        login_password=findViewById(R.id.password);
        login_login=findViewById(R.id.login);

        zhuce=findViewById(R.id.zhuce);
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(login.this,register.class);
                startActivity(intent);
            }
        });
        login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account=login_account.getText().toString();
                final String password=login_password.getText().toString();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                        try {
                            response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            user user =gson.fromJson(responseData, com.example.lego.user.class);
                            code=user.getCode();
                            msg=user.getMsg();


                            if(code==200)
                            {
                                com.example.lego.user.DataBean data = user.getData();
                                String token=data.getToken();
                                SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("account",account);
                                editor.putString("password",password);
                                editor.putString("token",token);
                                editor.putBoolean("islogin",true);
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
                                String head =userData.getHead();
                                String myName = userData.getName();
                                boolean mySex = userData.isSex();
                                String myInfo = userData.getInfo();
                                SharedPreferences.Editor editor1=sharedPreferences.edit();
                                editor1.putString("head",head);
                                editor1.putString("name",myName);
                                editor1.putBoolean("sex",mySex);
                                editor1.putString("info",myInfo);
                                editor1.commit();
                                editor.commit();
                                finish();
                                Intent intent= new Intent(login.this,MainActivity.class);
                                startActivity(intent);
                            }
                            Looper.prepare();
                            Toast.makeText(login.this,msg,Toast.LENGTH_SHORT).show();
                            Looper.loop();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

            }
        });





    }
}