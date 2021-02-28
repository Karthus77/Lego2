package com.example.lego;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StoreFragment extends Fragment {
    List<Map<String, Object>> list=new ArrayList<>();
    RecyclerView recyclerView;
    Button create;
    RefreshLayout refreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView=getActivity().findViewById(R.id.recyclerView1);
        refreshLayout=getActivity().findViewById(R.id.refreshLayout1);
        create=getActivity().findViewById(R.id.upload);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Create_good.class);
                startActivity(intent);
            }
        });
        final SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(100);
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Gson gson = new Gson();
                            String token =sharedPreferences.getString("token",String.valueOf(123));
                            Request request = new Request.Builder()
                                    .addHeader("Authorization",token)
                                    .url("http://49.232.214.94/api/goods")
                                    .build();
                            Response response = okHttpClient.newCall(request).execute();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"没有更多数据了",Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    Map<String,Object> map=new HashMap<>();
                                    map.put("1",1);
                                    map.put("2",2);
                                    list.add(map);
                                    StoreAdapter storeAdapter =new StoreAdapter(StoreFragment.this,list);
                                    recyclerView.setAdapter(storeAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                }
                            });

                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                final SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                list.clear();
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Gson gson = new Gson();
                            String token =sharedPreferences.getString("token",String.valueOf(123));
                            Request request = new Request.Builder()
                                    .addHeader("Authorization",token)
                                    .url("http://49.232.214.94/api/goods")
                                    .build();
                            Response response = okHttpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            good good =gson.fromJson(responseData, com.example.lego.good.class);
                            final com.example.lego.good.DataBean dataBean =good.getData();
                            final List<com.example.lego.good.DataBean.GoodsBean> goodsBeanList = dataBean.getGoods();
                            list.clear();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(goodsBeanList.size()==0)
                                    {
                                        Map<String,Object> map=new HashMap<>();
                                        map.put("1",1);
                                        list.add(map);
                                    }
                                    else{

                                        for(int i=0;i<goodsBeanList.size();i++)
                                        {
                                            Map<String,Object> map=new HashMap<>();
                                            com.example.lego.good.DataBean.GoodsBean goodsBean =goodsBeanList.get(i);
                                            String name=goodsBean.getName();
                                            String url=goodsBean.getImg();
                                            int id =goodsBean.getGood_id();
                                            int number=goodsBean.getQuantity();
                                            String price=goodsBean.getPrice();
                                            map.put("id",id);
                                            map.put("name",name);
                                            map.put("price",price);
                                            map.put("image",url);
                                            map.put("number",number);
                                            list.add(map);
                                        }}
                                    StoreAdapter storeAdapter =new StoreAdapter(StoreFragment.this,list);
                                    recyclerView.setAdapter(storeAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


                                }
                            });


                        } catch (Exception e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    Map<String,Object> map=new HashMap<>();
                                    map.put("1",1);
                                    map.put("2",2);
                                    list.add(map);
                                    StoreAdapter storeAdapter =new StoreAdapter(StoreFragment.this,list);
                                    recyclerView.setAdapter(storeAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        final SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Gson gson = new Gson();
                    String token =sharedPreferences.getString("token",String.valueOf(123));
                    Request request = new Request.Builder()
                            .addHeader("Authorization",token)
                            .url("http://49.232.214.94/api/goods")
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    good good =gson.fromJson(responseData, com.example.lego.good.class);
                    final com.example.lego.good.DataBean dataBean =good.getData();
                    final List<com.example.lego.good.DataBean.GoodsBean> goodsBeanList = dataBean.getGoods();
                    list.clear();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(goodsBeanList.size()==0)
                            {
                                Map<String,Object> map=new HashMap<>();
                                map.put("1",1);
                                list.add(map);
                            }
                            else{

                                for(int i=0;i<goodsBeanList.size();i++)
                                {
                                    Map<String,Object> map=new HashMap<>();
                                    com.example.lego.good.DataBean.GoodsBean goodsBean =goodsBeanList.get(i);
                                    String name=goodsBean.getName();
                                    String url=goodsBean.getImg();
                                    int id =goodsBean.getGood_id();
                                    int number=goodsBean.getQuantity();
                                    String price=goodsBean.getPrice();
                                    map.put("id",id);
                                    map.put("name",name);
                                    map.put("price",price);
                                    map.put("image",url);
                                    map.put("number",number);
                                    list.add(map);
                                }}
                            StoreAdapter storeAdapter =new StoreAdapter(StoreFragment.this,list);
                            recyclerView.setAdapter(storeAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


                        }
                    });


                } catch (Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list.clear();
                            Map<String,Object> map=new HashMap<>();
                            map.put("1",1);
                            map.put("2",2);
                            list.add(map);
                            StoreAdapter storeAdapter =new StoreAdapter(StoreFragment.this,list);
                            recyclerView.setAdapter(storeAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}