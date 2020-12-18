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

public class carAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private CarFragment context;
    private List<Map<String, Object>> list;
    private View inflater;
    public carAdapter(CarFragment context, List<Map<String, Object>> list) {
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
        else if(size==3)
        {
            return  net;
        }
        else {
            return  yes;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==yes)
        {
            inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
            RecyclerView.ViewHolder ViewHolder = new carAdapter.ViewHolder(inflater);
            return ViewHolder;}
        else if(viewType==net)
        {
            inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nonet,parent,false);
            carAdapter.netHolder netHolder = new carAdapter.netHolder(inflater);
            return  netHolder;
        }
        else
        {
            inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.nobuy_item,parent,false);
            carAdapter.noHolder noHolder = new carAdapter.noHolder(inflater);
            return  noHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType=getItemViewType(position);
        if(viewType==yes) {

            carAdapter.ViewHolder viewHolder = (carAdapter.ViewHolder) holder;
            String name = list.get(position).get("name").toString();
            String price = list.get(position).get("price").toString();
            String number = list.get(position).get("number").toString();
            final String id =list.get(position).get("id").toString();
            Glide.with(context).load(list.get(position).get("image")).into(viewHolder.order_image);
            viewHolder.order_name.setText(name);
            viewHolder.order_price.setText(price);
            viewHolder.order_number.setText(number);
            viewHolder.myCar.setOnClickListener(new View.OnClickListener() {
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
        return list.size();
    }
class ViewHolder extends RecyclerView.ViewHolder {
    ImageView order_image;
    TextView order_name;
    TextView order_price;
    TextView order_number;
    LinearLayout myCar;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        myCar=itemView.findViewById(R.id.myCar);
        order_image = itemView.findViewById(R.id.order_image);
        order_name = itemView.findViewById(R.id.order_name);
        order_price = itemView.findViewById(R.id.order_price);
        order_number =itemView.findViewById(R.id.order_number);
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

