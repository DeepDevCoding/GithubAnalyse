package com.huabing.githubanalyse.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huabing.githubanalyse.Bean.Collection;
import com.huabing.githubanalyse.CodeDetailActivity;
import com.huabing.githubanalyse.R;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 30781 on 2017/7/5.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder>{
    private Context mContext;
    private List<Collection> collectionList;
    public CollectionAdapter(List<Collection> collectionList)
    {
        this.collectionList=collectionList;
    }
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cvCardView;
        ImageView ivUser;
        TextView tvProgram;
        TextView tvType;
        TextView tvStar;
        ImageView ivDelete;
        public ViewHolder(View view)
        {
            super(view);
            cvCardView=(CardView)view.findViewById(R.id.cv_cardview);
            ivUser=(ImageView)view.findViewById(R.id.iv_user);
            tvProgram=(TextView)view.findViewById(R.id.tv_program);
            tvType=(TextView)view.findViewById(R.id.tv_type);
            tvStar=(TextView)view.findViewById(R.id.tv_star);
            ivDelete=(ImageView)view.findViewById(R.id.iv_delete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext=parent.getContext();
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_collection,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position=holder.getAdapterPosition();
                Collection collection=collectionList.get(position);
                final String program=collection.getProgram();
                AlertDialog.Builder dialog=new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Light_Dialog_Alert);
                dialog.setTitle("删除");
                dialog.setMessage("是否删除该收藏?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DataSupport.deleteAll(Collection.class,"program=?",program);
                        collectionList.remove(position);
                        notifyItemRemoved(position);
                    }
                });
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                    }
                });
                dialog.show();
            }
        });

        holder.cvCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Collection collection=collectionList.get(position);
                Intent intent=new Intent(mContext, CodeDetailActivity.class);
                intent.putExtra("program",collection.getProgram());
                mContext.startActivity(intent);
            }
        });



        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Collection collection=collectionList.get(position);
        holder.tvProgram.setText(collection.getProgram());
        holder.tvType.setText(collection.getType());
        holder.tvStar.setText(""+collection.getStar());
        Glide.with(mContext).load(collection.getUser()).into(holder.ivUser);
    }

    @Override
    public int getItemCount()
    {
        return collectionList.size();
    }
}
