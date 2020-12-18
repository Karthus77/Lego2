package com.example.lego;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
class user_register{
    String account;
    String password;
}

public class register extends AppCompatActivity {
    EditText register_account;
    EditText register_password;
    ImageView back;
    Button sign_up;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_register);
        register_account=findViewById(R.id.account_register);
        register_password=findViewById(R.id.password_register);
        sign_up=findViewById(R.id.sign_up);
        back=findViewById(R.id.backToLogin);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account=register_account.getText().toString();
                final String password=register_password.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Gson gson = new Gson();
                        user_register use = new user_register();
                        use.account=account;
                        use.password=password;
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody requestBody =RequestBody.create(JSON,gson.toJson(use));
                        Request request = new Request.Builder()
                                .url("http://49.232.214.94/api/register")
                                .post(requestBody)
                                .build();
                        Response response = null;
                        try {
                            response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            user user =gson.fromJson(responseData, com.example.lego.user.class);
                            msg=user.getMsg();
                            int code=user.getCode();
                            if(code==200)
                            {
                                finish();
                                Intent intent=new Intent(register.this,login.class);
                                startActivity(intent);

                            }
                            Looper.prepare();
                            Toast.makeText(register.this,msg,Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }}

