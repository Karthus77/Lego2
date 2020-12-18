package com.example.lego;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class StoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private StoreFragment context;
    private List<Map<String, Object>> list;
    private View inflater;
    public StoreAdapter(StoreFragment context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }
    private static final int no = 0;
    private static final int yes = 1;
    private static final int net=2;

    @Override
    public int getItemViewType(int position) {
        int size=list.get(position).size();
        if(size==1)
        {
            return  no;
        }
        else if(size==2)
        {
            return net;
        }
        else {
            return  yes;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==yes)
        {
        inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mygood, parent, false);
        RecyclerView.ViewHolder ViewHolder = new ViewHolder(inflater);
        return ViewHolder;}
        else if(viewType==net)
        {
            inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nonet,parent,false);
            StoreAdapter.netHolder netHolder = new StoreAdapter.netHolder(inflater);
            return  netHolder;
        }
        else
        {
            inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.nogood_item,parent,false);
            noHolder noHolder = new noHolder(inflater);
            return  noHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType=getItemViewType(position);
        if(viewType==yes) {

            StoreAdapter.ViewHolder viewHolder = (StoreAdapter.ViewHolder) holder;
            String name = list.get(position).get("name").toString();
            String price = list.get(position).get("price").toString();
            String number = list.get(position).get("number").toString();
            final String id =list.get(position).get("id").toString();
            Glide.with(context).load(list.get(position).get("image")).into(viewHolder.good_image);
            viewHolder.good_name.setText(name);
            viewHolder.good_price.setText(price);
            viewHolder.good_number.setText(number);
            viewHolder.good_revise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(context.getActivity(),Upload_good.class);
                    Bundle bd=new Bundle();
                    bd.putString("id",id);
                    intent.putExtras(bd);
                    context.startActivity(intent);
                }
            });
            viewHolder.myGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(context.getActivity(),good_info.class);
                    Bundle bd=new Bundle();
                    bd.putString("id",id);
                    intent.putExtras(bd);
                    context.startActivity(intent);
                }
            });
        }
        else {

        }

    }

    @Override
    public int getItemCount() {
        return  list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView good_image;
        TextView good_name;
        TextView good_price;
        TextView good_number;
        Button good_revise;
        LinearLayout myGood;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myGood=itemView.findViewById(R.id.myGood);
            good_revise=itemView.findViewById(R.id.myGood_revise);
            good_image = itemView.findViewById(R.id.myGood_image);
            good_name = itemView.findViewById(R.id.myGood_name);
            good_price = itemView.findViewById(R.id.myGood_price);
            good_number =itemView.findViewById(R.id.myGood_number);
        }


    }
    class noHolder extends RecyclerView.ViewHolder{

        public noHolder (View view){
            super(view);
        }
    }
    class netHolder extends RecyclerView.ViewHolder{

        public netHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
