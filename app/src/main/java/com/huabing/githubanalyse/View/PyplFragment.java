package com.huabing.githubanalyse.View;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.getbase.floatingactionbutton.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.huabing.githubanalyse.Adapter.PyplAdapter;
import com.huabing.githubanalyse.Bean.Db;
import com.huabing.githubanalyse.Bean.Ide;
import com.huabing.githubanalyse.Bean.Lang;
import com.huabing.githubanalyse.Bean.Ode;
import com.huabing.githubanalyse.Bean.Pypl;
import com.huabing.githubanalyse.R;
import com.huabing.githubanalyse.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class PyplFragment extends Fragment implements View.OnClickListener{
    private TextView tvType;
    private List<Pypl> pyplList;
    private RecyclerView rvPypl;
    private PyplAdapter adapter;
    private int typeNum;
    private FloatingActionButton fabLang;
    private FloatingActionButton fabIde;
    private FloatingActionButton fabOde;
    private FloatingActionButton fabDb;

    public PyplFragment() {

    }

    public static PyplFragment newInstance() {
        PyplFragment fragment = new PyplFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pypl, container, false);
        //标题栏
        tvType=(TextView)view.findViewById(R.id.tv_type);
        //浮动按钮
        fabLang=(FloatingActionButton)view.findViewById(R.id.fab_lang);
        fabLang.setOnClickListener(this);
        fabIde=(FloatingActionButton)view.findViewById(R.id.fab_ide);
        fabIde.setOnClickListener(this);
        fabOde=(FloatingActionButton)view.findViewById(R.id.fab_ode);
        fabOde.setOnClickListener(this);
        fabDb=(FloatingActionButton)view.findViewById(R.id.fab_db);
        fabDb.setOnClickListener(this);

        pyplList=new ArrayList<>();

        rvPypl=(RecyclerView)view.findViewById(R.id.rv_pypl);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        rvPypl.setLayoutManager(manager);
        adapter=new PyplAdapter(pyplList);
        rvPypl.setAdapter(adapter);

        //获取当前时间
        Calendar calendar=Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String nowDate=year+"-"+month;
        //比较时间
        SharedPreferences spf=getActivity().getSharedPreferences("pyplDate", MODE_PRIVATE);
        String date=spf.getString("date",null);
        if(date!=null&&date.equals(nowDate))  //时间相等，即今天
        {
            //初始化默认加载Language
            List<Lang> tempList = DataSupport.findAll(Lang.class);
            int length = tempList.size();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    Pypl pypl = new Pypl();
                    pypl.setRank(tempList.get(i).getRank());
                    pypl.setChange(tempList.get(i).getChange());
                    pypl.setType(tempList.get(i).getLanguage());
                    pypl.setShare(tempList.get(i).getShare());
                    pypl.setTrend(tempList.get(i).getTrend());
                    pyplList.add(pypl);
                }
                adapter.notifyDataSetChanged();
            } else {
                typeNum = 0;
                getJson(typeNum);
            }
        }
        else  //时间不是这个月则删除数据库数据
        {
            DataSupport.deleteAll(Pypl.class);
            //写入时间
            SharedPreferences.Editor editor=getActivity().getSharedPreferences("pyplDate",MODE_PRIVATE).edit();
            editor.putString("date",nowDate);
            editor.apply();
            typeNum = 0;
            getJson(typeNum);
        }
        return view;
    }

    //开启线程获取json数据并最后处理
    private void getJson(final int type)
    {
        //选择地址
        String address=null;
        if(typeNum==0)
            address="http://123.207.38.178/language.json";
        else if(typeNum==1)
            address="http://123.207.38.178/ide.json";
        else if(typeNum==2)
            address="http://123.207.38.178/ode.json";
        else if(typeNum==3)
            address="http://123.207.38.178/db.json";
        //开启子线程获取json数据
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data=response.body().string();
                gsonPaser(data);
                //UI显示
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pyplList.clear();
                        if(typeNum==0)
                        {
                            List<Lang> tempList=DataSupport.findAll(Lang.class);
                            int length=tempList.size();
                            if(length>0)
                            {
                                for(int i=0;i<length;i++)
                                {
                                    Pypl pypl=new Pypl();
                                    pypl.setRank(tempList.get(i).getRank());
                                    pypl.setChange(tempList.get(i).getChange());
                                    pypl.setType(tempList.get(i).getLanguage());
                                    pypl.setShare(tempList.get(i).getShare());
                                    pypl.setTrend(tempList.get(i).getTrend());
                                    pyplList.add(pypl);
                                }
                            }
                        }
                        else if(type==1)
                        {
                            List<Ide> tempList=DataSupport.findAll(Ide.class);
                            int length=tempList.size();
                            if(length>0)
                            {
                                for(int i=0;i<length;i++)
                                {
                                    Pypl pypl=new Pypl();
                                    pypl.setRank(tempList.get(i).getRank());
                                    pypl.setChange(tempList.get(i).getChange());
                                    pypl.setType(tempList.get(i).getIde());
                                    pypl.setShare(tempList.get(i).getShare());
                                    pypl.setTrend(tempList.get(i).getTrend());
                                    pyplList.add(pypl);
                                }
                            }
                        }
                        else if(type==2)
                        {
                            List<Ode> tempList=DataSupport.findAll(Ode.class);
                            int length=tempList.size();
                            if(length>0)
                            {
                                for(int i=0;i<length;i++)
                                {
                                    Pypl pypl=new Pypl();
                                    pypl.setRank(tempList.get(i).getRank());
                                    pypl.setChange(tempList.get(i).getChange());
                                    pypl.setType(tempList.get(i).getOde());
                                    pypl.setShare(tempList.get(i).getShare());
                                    pypl.setTrend(tempList.get(i).getTrend());
                                    pyplList.add(pypl);
                                }
                            }
                        }
                        else if(type==3)
                        {
                            List<Db> tempList=DataSupport.findAll(Db.class);
                            int length=tempList.size();
                            if(length>0)
                            {
                                for(int i=0;i<length;i++)
                                {
                                    Pypl pypl=new Pypl();
                                    pypl.setRank(tempList.get(i).getRank());
                                    pypl.setChange(tempList.get(i).getChange());
                                    pypl.setType(tempList.get(i).getDb());
                                    pypl.setShare(tempList.get(i).getShare());
                                    pypl.setTrend(tempList.get(i).getTrend());
                                    pyplList.add(pypl);
                                }
                            }
                        }
                        //刷新列表
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    //解析json数据并保存到数据库
    private void gsonPaser(String data)
    {
        try
        {
            JSONArray jsonArray=new JSONArray(data);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                if(typeNum==0)  //语言排行
                {
                    Lang lang = new Lang();
                    lang.setRank(jsonObject.getString("rank"));
                    int change = jsonObject.getInt("change");
                    if (change == 1)
                        lang.setChange(R.drawable.pypl_adapter_up);
                    else if (change == -1)
                        lang.setChange(R.drawable.pypl_adapter_down);
                    lang.setLanguage(jsonObject.getString("language"));
                    lang.setShare(jsonObject.getString("share"));
                    lang.setTrend(jsonObject.getString("trend"));
                    lang.save();
                }
                else if(typeNum==1)  //IDE排行
                {
                    Ide ide = new Ide();
                    ide.setRank(jsonObject.getString("rank"));
                    int change = jsonObject.getInt("change");
                    if (change == 1)
                        ide.setChange(R.drawable.pypl_adapter_up);
                    else if (change == -1)
                        ide.setChange(R.drawable.pypl_adapter_down);
                    ide.setIde(jsonObject.getString("ide"));
                    ide.setShare(jsonObject.getString("share"));
                    ide.setTrend(jsonObject.getString("trend"));
                    ide.save();
                }
                else if(typeNum==2)  //ODE排行
                {
                    Ode ode = new Ode();
                    ode.setRank(jsonObject.getString("rank"));
                    int change = jsonObject.getInt("change");
                    if (change == 1)
                        ode.setChange(R.drawable.pypl_adapter_up);
                    else if (change == -1)
                        ode.setChange(R.drawable.pypl_adapter_down);
                    ode.setOde(jsonObject.getString("ode"));
                    ode.setShare(jsonObject.getString("share"));
                    ode.setTrend(jsonObject.getString("trend"));
                    ode.save();
                }
                else if(typeNum==3)  //IDE排行
                {
                    Db db = new Db();
                    db.setRank(jsonObject.getString("rank"));
                    int change = jsonObject.getInt("change");
                    if (change == 1)
                        db.setChange(R.drawable.pypl_adapter_up);
                    else if (change == -1)
                        db.setChange(R.drawable.pypl_adapter_down);
                    db.setDb(jsonObject.getString("database"));
                    db.setShare(jsonObject.getString("share"));
                    db.setTrend(jsonObject.getString("trend"));
                    db.save();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        //先清空数据
        pyplList.clear();
        switch (v.getId())
        {
            case R.id.fab_lang:
                //标题栏更改
                tvType.setText("Language");
                //先从本地获取数据
                List<Lang> langList=DataSupport.findAll(Lang.class);
                int langLength=langList.size();
                if(langLength>0)
                {
                    for(int i=0;i<langLength;i++)
                    {
                        Pypl pypl=new Pypl();
                        pypl.setRank(langList.get(i).getRank());
                        pypl.setChange(langList.get(i).getChange());
                        pypl.setType(langList.get(i).getLanguage());
                        pypl.setShare(langList.get(i).getShare());
                        pypl.setTrend(langList.get(i).getTrend());
                        pyplList.add(pypl);
                    }
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    typeNum=0;
                    getJson(typeNum);
                }
                break;
            case R.id.fab_ide:
                //标题栏更改
                tvType.setText("IDE");
                //先从本地获取数据
                List<Ide> ideList=DataSupport.findAll(Ide.class);
                int ideLength=ideList.size();
                if(ideLength>0)
                {
                    for(int i=0;i<ideLength;i++)
                    {
                        Pypl pypl=new Pypl();
                        pypl.setRank(ideList.get(i).getRank());
                        pypl.setChange(ideList.get(i).getChange());
                        pypl.setType(ideList.get(i).getIde());
                        pypl.setShare(ideList.get(i).getShare());
                        pypl.setTrend(ideList.get(i).getTrend());
                        pyplList.add(pypl);
                    }
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    typeNum=1;
                    getJson(typeNum);
                }
                break;
            case R.id.fab_ode:
                //标题栏更改
                tvType.setText("ODE");
                //先从本地获取数据
                List<Ode> odeList=DataSupport.findAll(Ode.class);
                int odeLength=odeList.size();
                if(odeLength>0)
                {
                    for(int i=0;i<odeLength;i++)
                    {
                        Pypl pypl=new Pypl();
                        pypl.setRank(odeList.get(i).getRank());
                        pypl.setChange(odeList.get(i).getChange());
                        pypl.setType(odeList.get(i).getOde());
                        pypl.setShare(odeList.get(i).getShare());
                        pypl.setTrend(odeList.get(i).getTrend());
                        pyplList.add(pypl);
                    }
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    typeNum=2;
                    getJson(typeNum);
                }
                break;
            case R.id.fab_db:
                //标题栏更改
                tvType.setText("Database");
                //先从本地获取数据
                List<Db> dbList=DataSupport.findAll(Db.class);
                int dbLength=dbList.size();
                if(dbLength>0)
                {
                    for(int i=0;i<dbLength;i++)
                    {
                        Pypl pypl=new Pypl();
                        pypl.setRank(dbList.get(i).getRank());
                        pypl.setChange(dbList.get(i).getChange());
                        pypl.setType(dbList.get(i).getDb());
                        pypl.setShare(dbList.get(i).getShare());
                        pypl.setTrend(dbList.get(i).getTrend());
                        pyplList.add(pypl);
                    }
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    typeNum=3;
                    getJson(typeNum);
                }
                break;

        }
    }


}
