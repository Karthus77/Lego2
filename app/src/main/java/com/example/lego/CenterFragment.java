package com.example.lego;

import android.content.Context;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CenterFragment extends Fragment {
     List<Map<String, Object>> list=new ArrayList<>();
     RecyclerView recyclerView;
     RefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_center, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView=getActivity().findViewById(R.id.recyclerView);
        refreshLayout=getActivity().findViewById(R.id.refreshLayout);
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final OkHttpClient okHttpClient = new OkHttpClient();
                    Gson gson = new Gson();
                    Request request = new Request.Builder()
                            .url("http://49.232.214.94/api/goods")
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    final good good =gson.fromJson(responseData, com.example.lego.good.class);
                    final com.example.lego.good.DataBean dataBean =good.getData();
                    final List<com.example.lego.good.DataBean.GoodsBean> goodsBeanList = dataBean.getGoods();
                    list.clear();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for(int i=0;i<goodsBeanList.size();i++)
                            {
                                Map<String,Object> map=new HashMap<>();
                                com.example.lego.good.DataBean.GoodsBean goodsBean =goodsBeanList.get(i);
                                String name=goodsBean.getName();
                                int id =goodsBean.getGood_id();
                                String url=goodsBean.getImg();
                                String price=goodsBean.getPrice();
                                map.put("id",id);
                                map.put("name",name);
                                map.put("price",price);
                                map.put("image",url);
                                list.add(map);
                            }
                            GoodAdapter goodAdapter =new GoodAdapter(CenterFragment.this,list);
                            recyclerView.setAdapter(goodAdapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));


                        }
                    });

                } catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list.clear();
                            Map<String,Object> map=new HashMap<>();
                            map.put("1",1);
                            list.add(map);
                            GoodAdapter goodAdapter = new GoodAdapter(CenterFragment.this,list);
                            recyclerView.setAdapter(goodAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh( RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                list.clear();
                Thread thread =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final OkHttpClient okHttpClient = new OkHttpClient();
                            Gson gson = new Gson();
                            Request request = new Request.Builder()
                                    .url("http://49.232.214.94/api/goods")
                                    .build();
                            Response response = okHttpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            final good good =gson.fromJson(responseData, com.example.lego.good.class);
                            final com.example.lego.good.DataBean dataBean =good.getData();
                            final List<com.example.lego.good.DataBean.GoodsBean> goodsBeanList = dataBean.getGoods();
                            list.clear();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    for(int i=0;i<goodsBeanList.size();i++)
                                    {
                                        Map<String,Object> map=new HashMap<>();
                                        com.example.lego.good.DataBean.GoodsBean goodsBean =goodsBeanList.get(i);
                                        String name=goodsBean.getName();
                                        int id =goodsBean.getGood_id();
                                        String url=goodsBean.getImg();
                                        String price=goodsBean.getPrice();
                                        map.put("id",id);
                                        map.put("name",name);
                                        map.put("price",price);
                                        map.put("image",url);
                                        list.add(map);
                                    }
                                    GoodAdapter goodAdapter =new GoodAdapter(CenterFragment.this,list);
                                    recyclerView.setAdapter(goodAdapter);
                                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));


                                }
                            });

                        } catch (IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    Map<String,Object> map=new HashMap<>();
                                    map.put("1",1);
                                    list.add(map);
                                    GoodAdapter goodAdapter = new GoodAdapter(CenterFragment.this,list);
                                    recyclerView.setAdapter(goodAdapter);
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
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore( RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                Thread thread1 =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final OkHttpClient okHttpClient = new OkHttpClient();
                            Gson gson = new Gson();
                            Request request = new Request.Builder()
                                    .url("http://49.232.214.94/api/goods")
                                    .build();
                            Response response = okHttpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            final good good =gson.fromJson(responseData, com.example.lego.good.class);
                            final com.example.lego.good.DataBean dataBean =good.getData();
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
                                    list.add(map);
                                    GoodAdapter goodAdapter = new GoodAdapter(CenterFragment.this,list);
                                    recyclerView.setAdapter(goodAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                }
                            });

                            e.printStackTrace();
                        }
                    }
                });
                thread1.start();
            }
        });

    }
}