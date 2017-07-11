package com.huabing.githubanalyse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huabing.githubanalyse.Bean.SearchProgram;
import com.huabing.githubanalyse.CodeDetailActivity;
import com.huabing.githubanalyse.R;

import java.util.List;

/**
 * Created by 30781 on 2017/7/4.
 */

public class SearchProgramAdapter extends RecyclerView.Adapter<SearchProgramAdapter.ViewHolder>{
    private Context mContext;
    private List<SearchProgram> searchProgramList;

    public SearchProgramAdapter(List<SearchProgram> searchProgramList)
    {
        this.searchProgramList=searchProgramList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout llSearch;
        TextView tvName;
        TextView tvContent;
        TextView tvUpdate;
        TextView tvLanguage;
        ImageView ivUser;
        public ViewHolder(View view)
        {
            super(view);
            llSearch=(LinearLayout)view.findViewById(R.id.ll_search);
            tvName=(TextView)view.findViewById(R.id.tv_name);
            tvContent=(TextView)view.findViewById(R.id.tv_content);
            tvUpdate=(TextView)view.findViewById(R.id.tv_update);
            tvLanguage=(TextView)view.findViewById(R.id.tv_language);
            ivUser=(ImageView)view.findViewById(R.id.iv_user);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext=parent.getContext();
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_search_program,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        holder.llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                SearchProgram searchProgram=searchProgramList.get(position);
                Intent intent=new Intent(mContext, CodeDetailActivity.class);
                intent.putExtra("program",searchProgram.getName());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        SearchProgram searchProgram=searchProgramList.get(position);
        holder.tvName.setText(searchProgram.getName());
        holder.tvContent.setText(searchProgram.getContent());
        holder.tvUpdate.setText(searchProgram.getUpdate());
        holder.tvLanguage.setText(searchProgram.getLanguage());
        Glide.with(mContext).load(searchProgram.getUser()).into(holder.ivUser);
    }

    @Override
    public int getItemCount()
    {
        return searchProgramList.size();
    }
}
