package com.example.lego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class info_revise extends AppCompatActivity {
    private Button preserve;
    private EditText info;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_info_revise);
        preserve=findViewById(R.id.info_preserve);
        info=findViewById(R.id.info);
        back=findViewById(R.id.info_back);
        final SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        String id=sharedPreferences.getString("info",null);
        info.setText(id);
        preserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String revise_info =info.getText().toString();
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Gson gson = new Gson();
                            String token = sharedPreferences.getString("token", null);
                            String name = sharedPreferences.getString("name", null);
                            Boolean sex = sharedPreferences.getBoolean("sex", true);
                            user_upload user_upload = new user_upload();
                            user_upload.name = name;
                            user_upload.info = revise_info;
                            user_upload.sex = sex;
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody requestBody = RequestBody.create(JSON, gson.toJson(user_upload));
                            Request request = new Request.Builder()
                                    .addHeader("Authorization", token)
                                    .url("http://49.232.214.94/api/user")
                                    .put(requestBody)
                                    .build();
                            Response response = okHttpClient.newCall(request).execute();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(info_revise.this,"保存成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}