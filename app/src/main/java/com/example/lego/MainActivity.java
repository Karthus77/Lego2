package com.example.lego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    RelativeLayout relativeLayout1;
    RelativeLayout relativeLayout2;
    RelativeLayout relativeLayout3;
    RelativeLayout relativeLayout4;
    CenterFragment centerFragment= new CenterFragment();
    StoreFragment storeFragment= new StoreFragment();
    UserFragment userFragment= new UserFragment();
    CarFragment carFragment = new CarFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout1=findViewById(R.id.center_layout);
        relativeLayout2=findViewById(R.id.store_layout);
        relativeLayout3=findViewById(R.id.user_layout);
        relativeLayout4=findViewById(R.id.car_layout);
        relativeLayout1.setSelected(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,centerFragment).commit();
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout1.setSelected(true);
                relativeLayout2.setSelected(false);
                relativeLayout3.setSelected(false);
                relativeLayout4.setSelected(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,centerFragment).commit();
            }
        });
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout1.setSelected(false);
                relativeLayout2.setSelected(true);
                relativeLayout3.setSelected(false);
                relativeLayout4.setSelected(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,storeFragment).commit();
            }
        });
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout1.setSelected(false);
                relativeLayout2.setSelected(false);
                relativeLayout3.setSelected(true);
                relativeLayout4.setSelected(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,userFragment).commit();
            }
        });
        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout1.setSelected(false);
                relativeLayout2.setSelected(false);
                relativeLayout3.setSelected(false);
                relativeLayout4.setSelected(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,carFragment).commit();
            }
        });

    }
}