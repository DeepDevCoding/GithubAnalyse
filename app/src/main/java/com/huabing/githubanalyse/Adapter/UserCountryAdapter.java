package com.huabing.githubanalyse.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huabing.githubanalyse.Bean.Country;
import com.huabing.githubanalyse.R;

import java.util.List;

/**
 * Created by 30781 on 2017/7/3.
 */

public class UserCountryAdapter extends RecyclerView.Adapter<UserCountryAdapter.ViewHolder>{
    private Context context;
    private List<Country> countryList;
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName;
        TextView tvIncrease;
        ImageView ivImage;
        public ViewHolder(View view)
        {
            super(view);
            tvName=(TextView)view.findViewById(R.id.tv_user_country_name);
            tvIncrease=(TextView)view.findViewById(R.id.tv_user_country_increase);
            ivImage=(ImageView)view.findViewById(R.id.iv_user_country_image);
        }
    }

    public UserCountryAdapter(List<Country> countryList)
    {
        this.countryList=countryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_user_country,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position)
    {
        Country country=countryList.get(position);
        holder.tvName.setText(country.getName());
        holder.tvIncrease.setText(""+country.getIncrease());
        Glide.with(context).load(country.getImageUrl()).into(holder.ivImage);
    }

    @Override
    public int getItemCount()
    {
        return countryList.size();
    }
}
