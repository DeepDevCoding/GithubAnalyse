package com.huabing.githubanalyse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huabing.githubanalyse.Bean.AllLanguage;
import com.huabing.githubanalyse.Bean.C;
import com.huabing.githubanalyse.Bean.Collection;
import com.huabing.githubanalyse.Bean.Cplus;
import com.huabing.githubanalyse.Bean.Java;
import com.huabing.githubanalyse.Bean.JavaScript;
import com.huabing.githubanalyse.Bean.Php;
import com.huabing.githubanalyse.Bean.Python;
import com.huabing.githubanalyse.Bean.Ruby;
import com.huabing.githubanalyse.Bean.Swift;
import com.huabing.githubanalyse.CodeDetailActivity;
import com.huabing.githubanalyse.R;

import java.util.List;

/**
 * Created by 30781 on 2017/5/24.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    private Context mContext;
    private List<AllLanguage> languageList;
    private String strFragment;

    //初始化 ViewHolder
   static class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout llLayout;
        ImageView ivUser;
        TextView tvType;
        TextView tvProgram;
        TextView tvIntroduce;
        ImageView ivCollect;
        TextView tvStar;
        public ViewHolder(View view)
        {
            super(view);
            llLayout=(LinearLayout)view.findViewById(R.id.ll_layout);
            ivUser=(ImageView)view.findViewById(R.id.iv_user);
            tvType=(TextView)view.findViewById(R.id.tv_type);
            tvProgram=(TextView)view.findViewById(R.id.tv_program);
            tvIntroduce=(TextView)view.findViewById(R.id.tv_introduce);
            ivCollect=(ImageView)view.findViewById(R.id.iv_collect);
            tvStar=(TextView)view.findViewById(R.id.tv_star);
        }
    }
    public LanguageAdapter(List<AllLanguage> languageList,String strFragment)
    {
        this.languageList=languageList;
        this.strFragment=strFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext=parent.getContext();
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_language,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.llLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当前选中项
                int position=holder.getAdapterPosition();
                AllLanguage language=languageList.get(position);
                Intent intent=new Intent(mContext, CodeDetailActivity.class);
                intent.putExtra("program",language.getProgram());
                mContext.startActivity(intent);
            }
        });

        holder.ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivCollect.setImageResource(R.drawable.language_collect_click);
                int position=holder.getAdapterPosition();
                AllLanguage language=languageList.get(position);
                String program=language.getProgram();
                //找到数据库该项目位置
                if(strFragment.equals("AllLanguageFragment"))
                {
                    AllLanguage allLanguage=new AllLanguage();
                    allLanguage.setCollect(R.drawable.language_collect_click);
                    allLanguage.updateAll("program=?",program);
                }
                else if(strFragment.equals("CFragment"))
                {
                    C c=new C();
                    c.setCollect(R.drawable.language_collect_click);
                    c.updateAll("program=?",program);
                }
                else if(strFragment.equals("CplusFragment"))
                {
                    Cplus cplus=new Cplus();
                    cplus.setCollect(R.drawable.language_collect_click);
                    cplus.updateAll("program=?",program);
                }
                else if(strFragment.equals("JavaFragment"))
                {
                    Java java=new Java();
                    java.setCollect(R.drawable.language_collect_click);
                    java.updateAll("program=?",program);
                }
                else if(strFragment.equals("JavaScriptFragment"))
                {
                    JavaScript javaScript=new JavaScript();
                    javaScript.setCollect(R.drawable.language_collect_click);
                    javaScript.updateAll("program=?",program);
                }
                else if(strFragment.equals("PythonFragment"))
                {
                    Python python=new Python();
                    python.setCollect(R.drawable.language_collect_click);
                    python.updateAll("program=?",program);
                }
                else if(strFragment.equals("SwiftFragment"))
                {
                    Swift swift=new Swift();
                    swift.setCollect(R.drawable.language_collect_click);
                    swift.updateAll("program=?",program);
                }
                else if(strFragment.equals("PhpFragment"))
                {
                    Php php=new Php();
                    php.setCollect(R.drawable.language_collect_click);
                    php.updateAll("program=?",program);
                }
                else if(strFragment.equals("RubyFragment"))
                {
                    Ruby ruby=new Ruby();
                    ruby.setCollect(R.drawable.language_collect_click);
                    ruby.updateAll("program=?",program);
                }
                Collection collection=new Collection();
                collection.setUser(language.getUser());
                collection.setType(language.getType());
                collection.setProgram(language.getProgram());
                collection.setIntroduce(language.getIntroduce());
                collection.setStar(language.getStar());
                collection.save();
                Toast.makeText(mContext,"已收藏",Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position)
    {
        AllLanguage language=languageList.get(position);
        holder.tvProgram.setText(language.getProgram());
        holder.tvIntroduce.setText(language.getIntroduce());
        holder.tvType.setText(language.getType());
        holder.tvStar.setText(""+language.getStar());
        holder.ivCollect.setImageResource(language.getCollect());
        Glide.with(mContext).load(language.getUser()).into(holder.ivUser);
    }

    @Override
    public int getItemCount()
    {
        return languageList.size();
    }

}
