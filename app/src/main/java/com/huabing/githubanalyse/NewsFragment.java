package com.huabing.githubanalyse;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huabing.githubanalyse.Adapter.NewsAdapter;
import com.huabing.githubanalyse.Bean.News;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NewsFragment extends Fragment implements View.OnClickListener{
    private ImageView ivTimeLeft;
    private TextView tvTimeNow;
    private ImageView ivTimeRight;
    private ImageView ivMoreDate;
    private TextView tvLoading;
    private DatePickerDialog dpdDialog;
    private RecyclerView rvNews;
    private List<News> newsList;
    private NewsAdapter adapter;

    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_news, container, false);
        //初始化顶部控件
        ivTimeLeft=(ImageView)view.findViewById(R.id.iv_time_left);
        ivTimeLeft.setOnClickListener(this);
        tvTimeNow=(TextView)view.findViewById(R.id.tv_time_now);
        ivTimeRight=(ImageView)view.findViewById(R.id.iv_time_right);
        ivTimeRight.setOnClickListener(this);
        //加载控件
        tvLoading=(TextView)view.findViewById(R.id.tv_loading);
        //获取当前时间
        Calendar calendar=Calendar.getInstance();
        final int calYear=calendar.get(Calendar.YEAR);
        int calMonth=calendar.get(Calendar.MONTH);
        int calDay=calendar.get(Calendar.DAY_OF_MONTH);
        //设置当前日期
        SharedPreferences pref=getActivity().getSharedPreferences("date",MODE_PRIVATE);
        String currentTime=pref.getString("currentDate",null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        if(currentTime==null)
            tvTimeNow.setText(sdf.format(calendar.getTime()));
        else
            tvTimeNow.setText(currentTime);
        //初始化DatePickerDialog
        dpdDialog=new DatePickerDialog(getActivity(),DatePickerDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DATE,dayOfMonth);
                String date=sdf.format(c.getTime());
                if(!date.equals(tvTimeNow.getText().toString())) {
                    tvTimeNow.setText(date);
                    tvLoading.setText("加载中，请稍候......");
                    //写入时间
                    SharedPreferences.Editor editor=getActivity().getSharedPreferences("date", MODE_PRIVATE).edit();
                    editor.putString("currentDate",date);
                    editor.apply();
                    //清空列表
                    newsList.clear();
                    adapter.notifyDataSetChanged();
                    //清空当前数据库
                    DataSupport.deleteAll(News.class);
                    //加载网页
                    String strMonth;
                    String strDay;
                    strMonth=(month<10)?("0"+(month+1)):(""+(month+1));
                    strDay=(dayOfMonth<10)?("0"+(dayOfMonth)):(""+(dayOfMonth));
                    String leftAddress = "http://www.open-open.com/github/view/github"+year+"-"+strMonth+"-"+strDay+".html";
                    MyTask leftTask = new MyTask();
                    leftTask.execute(leftAddress);
                }
            }
        },calYear,calMonth,calDay);
        //日历范围
        Calendar minCal=Calendar.getInstance();
        minCal.add(Calendar.MONTH,-2);
        DatePicker picker=dpdDialog.getDatePicker();
        picker.setMinDate(minCal.getTimeInMillis());
        picker.setMaxDate(calendar.getTimeInMillis());
        //日历选择
        ivMoreDate=(ImageView)view.findViewById(R.id.iv_more_date);
        ivMoreDate.setOnClickListener(this);
        //新闻列表
        newsList=new ArrayList<>();
        //RecyclerView初始化
        rvNews=(RecyclerView)view.findViewById(R.id.rv_news);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        rvNews.setLayoutManager(manager);
        adapter=new NewsAdapter(newsList);
        rvNews.setAdapter(adapter);

        List<News> tempList=DataSupport.findAll(News.class);
        if(tempList.size()>0)
        {
            newsList.clear();
            int length=tempList.size();
            for(int i=0;i<length;i++)
                newsList.add(tempList.get(i));
            adapter.notifyDataSetChanged();
        }
        else
        {
            String strMonth;
            String strDay;
            strMonth=(calMonth<10)?("0"+(calMonth+1)):(""+(calMonth+1));
            strDay=(calDay<10)?("0"+calDay):(""+calDay);
            String address = "http://www.open-open.com/github/view/github"+calYear+"-"+strMonth+"-"+strDay+".html";
            MyTask task = new MyTask();
            task.execute(address);
        }
        return view;
    }

    public void getNewsWithJsoup(String address)
    {
        try
        {
            Document doc = Jsoup.connect(address).get();
            //作者头像
            Elements newEles = doc.select("tbody tr td");
            int length=newEles.size()/4;
            for(int i=0;i<length;i+=4)
            {
                News news=new News();
                news.setStyle(newEles.get(i).text());
                news.setStar(newEles.get(i+1).text());
                news.setName(newEles.get(i+2).text());
                news.setContent(newEles.get(i+3).text());
                news.save();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public class MyTask extends AsyncTask<String,Integer,String>
    {
        @Override
        protected void onPreExecute()
        {
            tvLoading.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... params)
        {
            getNewsWithJsoup(params[0]);
            return null;
        }
        @Override
        protected void onPostExecute(String result)
        {
            List<News> tempList= DataSupport.findAll(News.class);
            if(tempList.size()>0)
            {
                tvLoading.setVisibility(View.GONE);
                int length=tempList.size();
                for(int i=0;i<length;i++)
                    newsList.add(tempList.get(i));
                adapter.notifyDataSetChanged();
            }
            else
            {
                tvLoading.setText("今天数据未更新，请看前一天!");
            }
        }
    }

    //点击事件
    public void onClick(View v)
    {
        String date=tvTimeNow.getText().toString();
        String[] strArray=date.split("-");
        int year=Integer.valueOf(strArray[0]).intValue();
        int month=Integer.valueOf(strArray[1]).intValue();
        int day=Integer.valueOf(strArray[2]).intValue();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DATE,day);
        switch (v.getId())
        {
            case R.id.iv_time_left:
                //显示加载中
                tvLoading.setText("加载中，请稍候......");
                //清空列表
                newsList.clear();
                adapter.notifyDataSetChanged();
                //清空当前数据库
                DataSupport.deleteAll(News.class);
                //日期减一
                c.add(Calendar.DATE,-1);
                tvTimeNow.setText(sdf.format(c.getTime()));
                //SharedPreferences写入时间
                SharedPreferences.Editor leftEditor=getActivity().getSharedPreferences("date", MODE_PRIVATE).edit();
                leftEditor.putString("currentDate",sdf.format(c.getTime()));
                leftEditor.apply();
                //选择的时间
                String leftMonth;
                String leftDay;
                leftMonth=(month<10)?("0"+month):(""+month);
                leftDay=(day<10)?("0"+(day-1)):(""+(day-1));
                //地址
                String leftAddress = "http://www.open-open.com/github/view/github"+year+"-"+leftMonth+"-"+leftDay+".html";
                MyTask leftTask = new MyTask();
                leftTask.execute(leftAddress);
                break;
            case R.id.iv_time_right:
                //显示加载中
                tvLoading.setText("加载中，请稍候......");
                Calendar calendar=Calendar.getInstance();
                if((day+1)>calendar.get(Calendar.DAY_OF_MONTH))
                {
                    Toast.makeText(getActivity(),"当前数据已是最新！",Toast.LENGTH_SHORT).show();
                    tvLoading.setText("数据已是最新！");
                    break;
                }
                //清空列表
                newsList.clear();
                adapter.notifyDataSetChanged();
                //清空当前数据库
                DataSupport.deleteAll(News.class);
                //日期加一
                c.add(Calendar.DATE, 1);
                tvTimeNow.setText(sdf.format(c.getTime()));
                //SharedPreferences写入时间
                SharedPreferences.Editor rightEditor=getActivity().getSharedPreferences("date", MODE_PRIVATE).edit();
                rightEditor.putString("currentDate",sdf.format(c.getTime()));
                rightEditor.apply();
                //选择的时间
                String rightMonth;
                String rightDay;
                rightMonth=(month<10)?("0"+month):(""+month);
                rightDay=(day<10)?("0"+(day+1)):(""+(day+1));
                //地址
                String rightAddress = "http://www.open-open.com/github/view/github"+year+"-"+rightMonth+"-"+rightDay+".html";
                MyTask rightTask = new MyTask();
                rightTask.execute(rightAddress);
                break;
            case R.id.iv_more_date:
                dpdDialog.show();
                break;
            default:
                break;
        }

    }
}
