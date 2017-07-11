package com.huabing.githubanalyse.View;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huabing.githubanalyse.Adapter.TiobeAdapter;
import com.huabing.githubanalyse.Bean.Tiobe;
import com.huabing.githubanalyse.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TiobeFragment extends Fragment {

    private TextView tvYear;
    private TextView tvOldYear;
    private List<Tiobe> tiobeList;
    private RecyclerView rvTiobe;
    private TiobeAdapter adapter;

    public TiobeFragment() {

    }

    public static TiobeFragment newInstance() {
        TiobeFragment fragment = new TiobeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tiobe, container, false);
        tvYear=(TextView)view.findViewById(R.id.tv_year);
        tvOldYear=(TextView)view.findViewById(R.id.tv_old_year);
        Calendar c = Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int oldYear=year-1;
        tvYear.setText(""+year);
        tvOldYear.setText(""+oldYear);

        tiobeList=new ArrayList<>();
        rvTiobe=(RecyclerView)view.findViewById(R.id.rv_tiobe);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        rvTiobe.setLayoutManager(manager);
        adapter=new TiobeAdapter(tiobeList);
        rvTiobe.setAdapter(adapter);

        List<Tiobe> tempList=DataSupport.findAll(Tiobe.class);
        if(tempList.size()>0)
        {
            tiobeList.clear();//先清除缓存
            int length=tempList.size();
            for(int i=0;i<length;i++)
                tiobeList.add(tempList.get(i));
            adapter.notifyDataSetChanged();
        }
        else
        {
            String address = "https://www.tiobe.com/tiobe-index/";
            MyTask task = new MyTask();
            task.execute(address);
        }
        return view;
    }

    private class MyTask extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            getData(params[0]);
            return null;
        }
        @Override
        protected void onPostExecute(String result)
        {
            //更新UI
            List<Tiobe> tempList= DataSupport.findAll(Tiobe.class);
            if(tempList.size()>0)
            {
                int length=tempList.size();
                for(int i=0;i<length;i++)
                    tiobeList.add(tempList.get(i));
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void getData(String address)
    {
        try
        {
            Document doc= Jsoup.connect(address).get();

            Elements elements=doc.select("tbody tr td");
            for(int i=0;i<120;i+=6)
            {
                Tiobe tiobe=new Tiobe();
                String rank=elements.get(i).text();
                String oldRank=elements.get(i+1).text();
                tiobe.setRank(rank);
                tiobe.setOldRank(oldRank);

                int numRank=Integer.valueOf(rank).intValue();
                int numOldRank=Integer.valueOf(oldRank).intValue();
                if(numRank>numOldRank)
                    tiobe.setChangeImage(R.drawable.tiobe_adapter_down);
                else if(numRank<numOldRank)
                    tiobe.setChangeImage(R.drawable.tiobe_adapter_up);

                tiobe.setLanguage(elements.get(i+3).text());
                tiobe.setRatings(elements.get(i+4).text());
                tiobe.setChange(elements.get(i+5).text());
                tiobe.save();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
