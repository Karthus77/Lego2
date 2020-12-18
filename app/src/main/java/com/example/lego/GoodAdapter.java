package com.example.lego;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class GoodAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CenterFragment context;
    private List<Map<String, Object>> list;
    private View inflater;
    private static final int no = 0;
    private static final int yes = 1;

    public GoodAdapter(CenterFragment context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getItemViewType(int position) {
        int size=list.get(position).size();
        if(size==1)
        {
            return  no;
        }
        else {
            return  yes;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==yes)
        {
        inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_good, parent, false);
        RecyclerView.ViewHolder ViewHolder = new ViewHolder(inflater);
        return ViewHolder;}
        else
        {
            inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nonet,parent,false);
            GoodAdapter.netHolder netHolder = new GoodAdapter.netHolder(inflater);
            return  netHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType=getItemViewType(position);
        if(viewType==yes)
        {
        ViewHolder viewHolder = (ViewHolder) holder;
        String name = list.get(position).get("name").toString();
        String price = list.get(position).get("price").toString();
        final String id =list.get(position).get("id").toString();
        Glide.with(context).load(list.get(position).get("image")).into(viewHolder.good_image);
        viewHolder.good_name.setText(name);
        viewHolder.good_price.setText(price);
        viewHolder.Good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context.getActivity(),good_info.class);
                Bundle bd=new Bundle();
                bd.putString("id",id);
                intent.putExtras(bd);
                context.startActivity(intent);
            }
        });}
        else
        {

        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView good_image;
        TextView good_name;
        TextView good_price;
        LinearLayout Good;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Good=itemView.findViewById(R.id.good);
            good_image = itemView.findViewById(R.id.good_image);
            good_name = itemView.findViewById(R.id.good_name);
            good_price = itemView.findViewById(R.id.good_price);
        }


    }
    class netHolder extends RecyclerView.ViewHolder{

        public netHolder(@NonNull View itemView) {
            super(itemView);
        }
}}
