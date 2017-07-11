package com.huabing.githubanalyse.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huabing.githubanalyse.Bean.Pypl;
import com.huabing.githubanalyse.Bean.Tiobe;
import com.huabing.githubanalyse.R;

import java.util.List;

/**
 * Created by 30781 on 2017/6/7.
 */

public class TiobeAdapter extends RecyclerView.Adapter<TiobeAdapter.ViewHolder>{
    private Context context;
    private List<Tiobe> tiobeList;
    public TiobeAdapter(List<Tiobe> tiobeList)
    {
        this.tiobeList=tiobeList;
    }
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvRank;
        private TextView tvOldRank;
        private ImageView ivChange;
        private TextView tvLanguage;
        private TextView tvRatings;
        private TextView tvChange;
        public ViewHolder(View view)
        {
            super(view);
            tvRank=(TextView)view.findViewById(R.id.tv_rank);
            tvOldRank=(TextView)view.findViewById(R.id.tv_old_rank);
            tvChange=(TextView)view.findViewById(R.id.tv_change);
            ivChange=(ImageView)view.findViewById(R.id.iv_change);
            tvLanguage=(TextView)view.findViewById(R.id.tv_language);
            tvRatings=(TextView)view.findViewById(R.id.tv_ratings);
            tvChange=(TextView)view.findViewById(R.id.tv_change);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_tiobe,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Tiobe tiobe=tiobeList.get(position);
        String rank=tiobe.getRank();
        String oldRank=tiobe.getOldRank();
        int intRank=Integer.valueOf(rank).intValue();
        int intOldRank=Integer.valueOf(oldRank).intValue();

        /*if(intRank>intOldRank)
            holder.ivChange.setImageResource(R.drawable.tiobe_adapter_up);
        else if(intRank<intOldRank)
            holder.ivChange.setImageResource(R.drawable.tiobe_adapter_down);
        else
            holder.ivChange.setVisibility(View.INVISIBLE);*/
        holder.ivChange.setImageResource(tiobe.getChangeImage());
        holder.tvRank.setText(rank);
        holder.tvOldRank.setText(oldRank);
        holder.tvLanguage.setText(tiobe.getLanguage());
        holder.tvRatings.setText(tiobe.getRatings());
        holder.tvChange.setText(tiobe.getChange());

    }

    @Override
    public int getItemCount()
    {
        return tiobeList.size();
    }

}
