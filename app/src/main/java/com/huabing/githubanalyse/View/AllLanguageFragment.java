package com.huabing.githubanalyse.View;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.huabing.githubanalyse.Adapter.LanguageAdapter;
import com.huabing.githubanalyse.Bean.AllLanguage;
import com.huabing.githubanalyse.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllLanguageFragment extends Fragment {
    private TextView tvLoading;
    private RecyclerView rvLanguage;
    private List<AllLanguage> languageList;
    private LanguageAdapter adapter;
    private MaterialRefreshLayout mrlRefresh;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public AllLanguageFragment() {

    }

    public static AllLanguageFragment newInstance() {
        AllLanguageFragment fragment = new AllLanguageFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_all_language, container, false);
        //加载提示TextView
        tvLoading=(TextView)view.findViewById(R.id.tv_loading);

        //刷新控件MaterialRefreshLayout
        mrlRefresh=(MaterialRefreshLayout)view.findViewById(R.id.mrl_refresh);
        mrlRefresh.setLoadMore(true);
        //下拉刷新与上拉更多
        mrlRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
                mrlRefresh.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout)
            {
                //异步加载解析网页
                int end=languageList.size()+10;
                if(end<=40)
                {
                    String address = "https://github-trending.com/";
                    MyTask task = new MyTask();
                    task.execute(address);
                }
                else
                {
                    mrlRefresh.finishRefreshLoadMore();
                    //Toast.makeText(getActivity(), "暂无更多", Toast.LENGTH_SHORT).show();
                }
            }
        });

        languageList=new ArrayList<>();
        //RecyclerView初始化
        rvLanguage=(RecyclerView)view.findViewById(R.id.rv_language);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        rvLanguage.setLayoutManager(manager);
        adapter=new LanguageAdapter(languageList,"AllLanguageFragment");
        rvLanguage.setAdapter(adapter);

        //今天日期
        Calendar c = Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH)+1;
        int day=c.get(Calendar.DAY_OF_MONTH);
        String newDate=year+"-"+month+"-"+day;// 获取当日期

        pref= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String oldDate=pref.getString("allDate","");
        //不是今天
        if(!oldDate.equals(newDate))
        {
            editor=pref.edit();
            editor.putString("allDate",newDate);
            editor.apply();


            //先删除原始数据库
            DataSupport.deleteAll(AllLanguage.class);
            //异步加载解析网页
            String address = "https://github-trending.com/";
            MyTask task = new MyTask();
            task.execute(address);

        }
        else   //是今天
        {
            //临时List检测数据库中是否有数据
            List<AllLanguage> tempList = DataSupport.findAll(AllLanguage.class);
            if (tempList.size() > 0)    //如果有，直接从数据库中获取
            {
                tvLoading.setVisibility(View.GONE);  //隐藏加载
                for (int j = 0; j < tempList.size(); j++)
                    languageList.add(tempList.get(j));
                adapter.notifyDataSetChanged();
            }
            else                    //如果没有有，开始网页解析
            {
                //异步加载解析网页
                String address = "https://github-trending.com/";
                MyTask task = new MyTask();
                task.execute(address);
            }
        }
        return view;
    }

    //异步加载
    private class MyTask extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            getDataWithJsoup(params[0]);
            return null;
        }
        @Override
        protected void onPostExecute(String result)
        {
            languageList.clear(); //先清空
            List<AllLanguage> tempList= DataSupport.findAll(AllLanguage.class);
            if(tempList.size()>0)
            {
                tvLoading.setVisibility(View.GONE);  //隐藏加载
                for(int j=0;j<tempList.size();j++)
                    languageList.add(tempList.get(j));
                adapter.notifyDataSetChanged();
            }
            mrlRefresh.finishRefreshLoadMore();
            //Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_SHORT).show();
        }
    }

    //解析所有语言排行
    public void getDataWithJsoup(String address)
    {
        List<String> userList=new ArrayList<>();
        List<String> typeList=new ArrayList<>();
        List<String> programList=new ArrayList<>();
        List<String> introduceList=new ArrayList<>();
        List<String> starList=new ArrayList<>();
        int i=0;
        //int end=start+10;
        //Log.e("第一次加载","起始"+start+"终点"+end);
        int length=languageList.size();
        int end=length+10;
        try
        {
            Document doc = Jsoup.connect(address).get();
            //作者头像
            Elements userEles = doc.select("img[src*=https]");
            for (i = length; i < end; i++) {
                userList.add(userEles.get(i).attr("src"));
            }
            //语言类型
            Elements typeEles = doc.select(".content-lable");
            for (i = length; i < end; i++) {
                String[] array = typeEles.get(i).text().split(" ");
                typeList.add(array[0]);
                starList.add(array[1]);
            }
            //项目名
            Elements programEles = doc.select("h3 a[href*=https]");
            for (i = length; i < end; i++)
                programList.add(programEles.get(i).text());
            //项目介绍
            Elements introduceEles = doc.select(".caption p");
            for (i = length; i < end; i++)
                introduceList.add(introduceEles.get(i).text());
            //保存数据到数据库
            for (i = 0; i < 10; i++) {
                AllLanguage allLanguage = new AllLanguage();
                allLanguage.setUser(userList.get(i));
                allLanguage.setType(typeList.get(i));
                allLanguage.setProgram(programList.get(i));
                allLanguage.setIntroduce(introduceList.get(i));
                allLanguage.setCollect(R.drawable.language_collect);
                allLanguage.setStar(Integer.parseInt(starList.get(i)));
                allLanguage.save();
            }

            //清空所有List
            userList.clear();
            typeList.clear();
            programList.clear();
            introduceList.clear();
            starList.clear();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }




}
