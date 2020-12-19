package com.example.lego;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class CarFragment extends Fragment {
    List<Map<String, Object>> list=new ArrayList<>();
    RecyclerView recyclerView;
    RefreshLayout refreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView=getActivity().findViewById(R.id.recyclerView2);
        refreshLayout=getActivity().findViewById(R.id.refreshLayout2);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh( RefreshLayout refreshLayout) {
                final SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                list.clear();
                Thread thread =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            final OkHttpClient okHttpClient = new OkHttpClient();
                            Gson gson = new Gson();
                            String token =sharedPreferences.getString("token",String.valueOf(123));
                            Request request = new Request.Builder()
                                    .addHeader("Authorization",token)
                                    .url("http://49.232.214.94/api/order")
                                    .build();
                            Response response = okHttpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            order order =gson.fromJson(responseData, com.example.lego.order.class);
                            final com.example.lego.order.DataBean dataBean =order.getData();
                            final List<com.example.lego.order.DataBean.OrdersBean> ordersBeanList=dataBean.getOrders();
                            list.clear();
                            if(ordersBeanList.size()==0)
                            {
                                Map<String,Object> map=new HashMap<>();
                                map.put("1",1);
                                list.add(map);
                            }
                            else{

                                for(int i=0;i<ordersBeanList.size();i++)
                                {
                                    Map<String,Object> map=new HashMap<>();
                                    com.example.lego.order.DataBean.OrdersBean ordersBean=ordersBeanList.get(i);
                                    int id=ordersBean.getGood_id();
                                    int price=ordersBean.getGoods_price();
                                    int number=ordersBean.getGoods_count();
                                    map.put("id",id);
                                    map.put("price",price);
                                    map.put("number",number);
                                    OkHttpClient okHttpClient1 = new OkHttpClient();
                                    Gson gson1 =new Gson();
                                    Request request1 = new Request.Builder()
                                            .url("http://49.232.214.94/api/goods/"+id)
                                            .build();
                                    try {
                                        Response response1 = okHttpClient1.newCall(request1).execute();
                                        String responseData1 = response1.body().string();
                                        detail detail =gson1.fromJson(responseData1, com.example.lego.detail.class);
                                        com.example.lego.detail.DataBean dataBean1 =detail.getData();
                                        com.example.lego.detail.DataBean.GoodBean goodBean=dataBean1.getGood();
                                        String name=goodBean.getName();
                                        String url=goodBean.getImg();
                                        map.put("image",url);
                                        map.put("name",name);
                                        list.add(map);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }}
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    carAdapter carAdapter =new carAdapter(CarFragment.this,list);
                                    recyclerView.setAdapter(carAdapter);
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
                                    map.put("3",3);
                                    list.add(map);
                                    carAdapter carAdapter=new carAdapter(CarFragment.this,list);
                                    recyclerView.setAdapter(carAdapter);
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
                final SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                Thread thread =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            final OkHttpClient okHttpClient = new OkHttpClient();
                            Gson gson = new Gson();
                            String token =sharedPreferences.getString("token",String.valueOf(123));
                            Request request = new Request.Builder()
                                    .addHeader("Authorization",token)
                                    .url("http://49.232.214.94/api/order")
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
                                    map.put("3",3);
                                    list.add(map);
                                    carAdapter carAdapter=new carAdapter(CarFragment.this,list);
                                    recyclerView.setAdapter(carAdapter);
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
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    final OkHttpClient okHttpClient = new OkHttpClient();
                    Gson gson = new Gson();
                    String token =sharedPreferences.getString("token",String.valueOf(123));
                    Request request = new Request.Builder()
                            .addHeader("Authorization",token)
                            .url("http://49.232.214.94/api/order")
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    order order =gson.fromJson(responseData, com.example.lego.order.class);
                    final com.example.lego.order.DataBean dataBean =order.getData();
                    final List<com.example.lego.order.DataBean.OrdersBean> ordersBeanList=dataBean.getOrders();
                    list.clear();
                    if(ordersBeanList.size()==0)
                    {
                        Map<String,Object> map=new HashMap<>();
                        map.put("1",1);
                        list.add(map);
                    }
                    else{

                        for(int i=0;i<ordersBeanList.size();i++)
                        {
                            Map<String,Object> map=new HashMap<>();
                            com.example.lego.order.DataBean.OrdersBean ordersBean=ordersBeanList.get(i);
                            int id=ordersBean.getGood_id();
                            int price=ordersBean.getGoods_price();
                            int number=ordersBean.getGoods_count();
                            map.put("id",id);
                            map.put("price",price);
                            map.put("number",number);
                            OkHttpClient okHttpClient1 = new OkHttpClient();
                            Gson gson1 =new Gson();
                            Request request1 = new Request.Builder()
                                    .url("http://49.232.214.94/api/goods/"+id)
                                    .build();
                            try {
                                Response response1 = okHttpClient1.newCall(request1).execute();
                                String responseData1 = response1.body().string();
                                detail detail =gson1.fromJson(responseData1, com.example.lego.detail.class);
                                com.example.lego.detail.DataBean dataBean1 =detail.getData();
                                com.example.lego.detail.DataBean.GoodBean goodBean=dataBean1.getGood();
                                String name=goodBean.getName();
                                String url=goodBean.getImg();
                                map.put("image",url);
                                map.put("name",name);
                                list.add(map);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }}
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            carAdapter carAdapter =new carAdapter(CarFragment.this,list);
                            recyclerView.setAdapter(carAdapter);
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
                                map.put("3",3);
                                list.add(map);
                                carAdapter carAdapter=new carAdapter(CarFragment.this,list);
                                recyclerView.setAdapter(carAdapter);
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