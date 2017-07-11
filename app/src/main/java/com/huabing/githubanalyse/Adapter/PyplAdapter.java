package com.huabing.githubanalyse.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huabing.githubanalyse.Bean.Pypl;
import com.huabing.githubanalyse.R;

import java.util.List;

/**
 * Created by 30781 on 2017/6/6.
 */

public class PyplAdapter extends RecyclerView.Adapter<PyplAdapter.ViewHolder>{

    private List<Pypl> pyplList;
    public PyplAdapter(List<Pypl> pyplList)
    {
        this.pyplList=pyplList;
    }
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvRank;
        ImageView ivChange;
        TextView tvType;
        TextView tvShare;
        TextView tvTrend;
        public ViewHolder(View view)
        {
            super(view);
            tvRank=(TextView)view.findViewById(R.id.tv_rank);
            ivChange=(ImageView)view.findViewById(R.id.iv_change);
            tvType=(TextView)view.findViewById(R.id.tv_type);
            tvShare=(TextView)view.findViewById(R.id.tv_share);
            tvTrend=(TextView)view.findViewById(R.id.tv_trend);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pypl,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position)
    {
        Pypl pypl=pyplList.get(position);
        holder.tvRank.setText(pypl.getRank());
        holder.ivChange.setImageResource(pypl.getChange());
        holder.tvType.setText(pypl.getType());
        holder.tvShare.setText(pypl.getShare());
        holder.tvTrend.setText(pypl.getTrend());
    }

    @Override
    public int getItemCount()
    {
        return pyplList.size();
    }

}
