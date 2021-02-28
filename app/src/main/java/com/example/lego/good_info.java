package com.example.lego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
class user_order{
    int good_id;
    int goods_count;
}

public class good_info extends AppCompatActivity {
    ImageView back;
    Button add;
    Button subtract;
    ImageView img;
    TextView name;
    TextView info;
    TextView price;
    TextView restNumber;
    RelativeLayout buy;
    TextView number;
    private int num=1;
    private int length;
    int good_number;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_info);
        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        back =findViewById(R.id.backToMain);
        add=findViewById(R.id.info_add);
        subtract=findViewById(R.id.info_subtract);
        buy=findViewById(R.id.buy);
        number=findViewById(R.id.buy_number);
        restNumber=findViewById(R.id.number);
        img=findViewById(R.id.info_image);
        name=findViewById(R.id.info_name);
        info=findViewById(R.id.info_info);
        price=findViewById(R.id.info_price);
        final String token =sharedPreferences.getString("token",String.valueOf(11));
        number.setText(Integer.valueOf(num).toString());
        Intent it2=getIntent();
        final Bundle bd=it2.getExtras();
         final String id=bd.getString("id");
         final int Id=Integer.parseInt(id);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(length>10)
                {
                    Toast.makeText(good_info.this,"商品价格过高,不支持购买",Toast.LENGTH_SHORT).show();
                }
                else{
                Thread thread =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            OkHttpClient okHttpClient =new OkHttpClient();
                            user_order order =new user_order();
                            order.good_id=Id;
                            order.goods_count=num;
                            Gson gson =new Gson();
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody requestBody =RequestBody.create(JSON,gson.toJson(order));
                            Request request = new Request.Builder()
                                    .url("http://49.232.214.94/api/order")
                                    .addHeader("Authorization",token)
                                    .post(requestBody)
                                    .build();
                            Response response = null;
                            response = okHttpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            com.example.lego.order order1 =gson.fromJson(responseData, com.example.lego.order.class);
                            int code=order1.getCode();
                            if(code ==200)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(good_info.this,"已加入购物车",Toast.LENGTH_SHORT).show();
                                        restNumber.setText(Integer.valueOf(good_number-num).toString());
                                    }
                                });

                            }
                            else if(code==422)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(good_info.this,"不能购买自己的商品",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(good_info.this,"商品存货不够了",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();}


            }
        });
        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num>1)
                {
                    num--;
                    number.setText(Integer.valueOf(num).toString());
                }
                else
                {
                    Toast.makeText(good_info.this,"最少购买一份商品",Toast.LENGTH_SHORT).show();
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                number.setText(Integer.valueOf(num).toString());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient =new OkHttpClient();
                    Gson gson =new Gson();
                    Request request = new Request.Builder()
                            .url("http://49.232.214.94/api/goods/"+id)
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    final new_detail detail =gson.fromJson(responseData, com.example.lego.new_detail.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new_detail.DataBean dataBean = detail.getData();
                            new_detail.DataBean.GoodBean goodBean=dataBean.getGood();
                            String good_name=goodBean.getName();
                            String good_price=goodBean.getPrice();
                            length =good_price.length();
                            String good_info =goodBean.getInfo();
                            String good_url =goodBean.getImg();
                            good_number=goodBean.getQuantity();
                            restNumber.setText("剩余数量:"+good_number);
                            name.setText(good_name);
                            price.setText(good_price);
                            info.setText(good_info);
                            Glide.with(com.example.lego.good_info.this).load(good_url).into(img);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}