package com.huabing.githubanalyse.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;

import com.huabing.githubanalyse.Bean.AllLanguage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        /*updateGithubData();//更新数据
        //定时开启服务
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour=1*60*60*60*1000;
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent iten=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,iten,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);*/

        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    //从网上获取数据更新数据库
    public void updateGithubData()
    {
        /*DataSupport.deleteAll(AllLanguage.class);
        String address = "https://github-trending.com/";
        MyTask task=new MyTask();
        task.execute(address);*/
    }

    //异步加载
    private class MyTask extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            //getDataWithJsoup(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {

        }
    }

    /*解析所有语言排行
    public void getDataWithJsoup(String address)
    {
        List<String> userList=new ArrayList<>();
        List<String> typeList=new ArrayList<>();
        List<String> programList=new ArrayList<>();
        List<String> introduceList=new ArrayList<>();
        List<String> starList=new ArrayList<>();
        int i=0;
        try
        {
            Document doc= Jsoup.connect(address).get();
            //作者头像
            Elements userEles=doc.select("img[src*=https]");
            for(i=0;i<10;i++) {
                userList.add(userEles.get(i).attr("src"));
            }
            //语言类型
            Elements typeEles=doc.select(".content-lable");
            for(i=0;i<10;i++)
            {
                String[] array=typeEles.get(i).text().split(" ");
                typeList.add(array[0]);
                starList.add(array[1]);
            }
            //项目名
            Elements programEles=doc.select("h3 a[href*=https]");
            for(i=0;i<10;i++)
                programList.add(programEles.get(i).text());
            //项目介绍
            Elements introduceEles=doc.select(".caption p");
            for(i=0;i<10;i++)
                introduceList.add(introduceEles.get(i).text());
            //保存数据到数据库
            for(i=0;i<10;i++)
            {
                AllLanguage allLanguage=new AllLanguage();
                allLanguage.setUser(userList.get(i));
                allLanguage.setType(typeList.get(i));
                allLanguage.setProgram(programList.get(i));
                allLanguage.setIntroduce(introduceList.get(i));
                allLanguage.setStar(Integer.parseInt(starList.get(i)));
                allLanguage.save();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }*/
}
