package com.huabing.githubanalyse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huabing.githubanalyse.Bean.News;
import com.huabing.githubanalyse.CodeDetailActivity;
import com.huabing.githubanalyse.R;

import java.util.List;

/**
 * Created by 30781 on 2017/6/14.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private Context mContext;
    private List<News> newsList;
    public NewsAdapter(List<News> newsList)
    {
        this.newsList=newsList;
    }
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout llNews;
        TextView tvName;
        TextView tvContent;
        TextView tvStyle;
        TextView tvStar;
        public ViewHolder(View view)
        {
            super(view);
            llNews=(LinearLayout)view.findViewById(R.id.ll_news);
            tvName=(TextView)view.findViewById(R.id.tv_name);
            tvContent=(TextView)view.findViewById(R.id.tv_content);
            tvStyle=(TextView)view.findViewById(R.id.tv_style);
            tvStar=(TextView)view.findViewById(R.id.tv_star);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext=parent.getContext();
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_news,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                News news=newsList.get(position);
                Intent intent=new Intent(mContext, CodeDetailActivity.class);
                intent.putExtra("program",news.getName());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        News news=newsList.get(position);
        holder.tvName.setText(news.getName());
        holder.tvContent.setText(news.getContent());
        holder.tvStyle.setText(news.getStyle());
        holder.tvStar.setText(news.getStar());
    }

    @Override
    public int getItemCount()
    {
        return newsList.size();
    }

}
