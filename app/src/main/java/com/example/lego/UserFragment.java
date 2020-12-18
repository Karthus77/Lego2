package com.example.lego;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;


public class UserFragment extends Fragment {
    private RelativeLayout out;
    private ImageView revise;
    private TextView user_name;
    private ImageView user_sex;
    private ImageView user_head;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        out=getActivity().findViewById(R.id.out);
        user_head=getActivity().findViewById(R.id.user_head);
        revise=getActivity().findViewById(R.id.revise);
        user_name=getActivity().findViewById(R.id.user_name);
        user_sex=getActivity().findViewById(R.id.user_sex);
        final SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), com.example.lego.revise.class);
                startActivity(intent);
            }
        });
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("islogin",false);
                editor.commit();
                getActivity().finish();
                Intent intent=new Intent(getActivity(),login.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        final SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("name",null);
        String head=sharedPreferences.getString("head",null);
        Boolean sex=sharedPreferences.getBoolean("sex",true);
        user_name.setText(name);
        if(sex)
            user_sex.setImageResource(R.drawable.male);
        else
            user_sex.setImageResource(R.drawable.female);
        if(head==null ||head.length()==0) {
              }
        else {
            Glide.with(getActivity()).load("http://49.232.214.94/api/img/" + head).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(user_head);

        }
    }
}