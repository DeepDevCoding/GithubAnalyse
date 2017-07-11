package com.huabing.githubanalyse;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huabing.githubanalyse.Adapter.SearchProgramAdapter;
import com.huabing.githubanalyse.Bean.SearchProgram;
import com.huabing.githubanalyse.Json.SearchJson.SearchBean;
import com.huabing.githubanalyse.Util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView ivBack;
    private EditText etInput;
    private ImageView ivSearch;

    private ProgressBar pbSearch;

    private RecyclerView rvSearch;
    private List<SearchProgram> searchProgramList;
    private SearchProgramAdapter searchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //返回
        ivBack=(ImageView)findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        //输入
        etInput=(EditText)findViewById(R.id.et_input);
        //搜索
        ivSearch=(ImageView)findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(this);

        //搜索加载
        pbSearch=(ProgressBar)findViewById(R.id.pb_search);

        //搜索结果列表
        searchProgramList=new ArrayList<>();
        rvSearch=(RecyclerView)findViewById(R.id.rv_search);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        rvSearch.setLayoutManager(manager);
        searchAdapter=new SearchProgramAdapter(searchProgramList);
        rvSearch.setAdapter(searchAdapter);
    }

    /*在线项目搜索
    private void searchProgram(String address)
    {
        try
        {
            HttpUtil.sendOkHttpRequest(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result=response.body().string();
                    gsonPrase(result);
                    if(result!=null)
                    {
                        //gsonPrase(result);
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/

    //Gson解析
    private void gsonPrase(String jsonData)
    {
        Gson gson=new Gson();
        SearchBean searchBean=gson.fromJson(jsonData,SearchBean.class);
        int length=searchBean.getItems().size();
        if(length>0) {
            String regex="[a-zA-Z]";
            for (int i = 0; i < length; i++) {
                SearchProgram searchProgram = new SearchProgram();
                searchProgram.setName(searchBean.getItems().get(i).getFull_name());
                searchProgram.setContent(searchBean.getItems().get(i).getDescription());
                String update=searchBean.getItems().get(i).getPushed_at();
                if(update!=null) {
                    searchProgram.setUpdate(update.replaceAll(regex," "));
                }
                else {
                    searchProgram.setUpdate(" ");
                }
                searchProgram.setLanguage(searchBean.getItems().get(i).getLanguage());
                searchProgram.setUser(searchBean.getItems().get(i).getOwner().getAvatar_url());
                searchProgramList.add(searchProgram);
            }
        }
        else
        {
            Toast.makeText(this,"未查到该项目！",Toast.LENGTH_SHORT).show();
        }
    }

    //异步加载
    public class MyTask extends AsyncTask<String,Integer,String>
    {
        @Override
        protected void onPreExecute()
        {
            pbSearch.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... params)
        {
            //searchProgram(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            pbSearch.setVisibility(View.GONE);
            searchAdapter.notifyDataSetChanged();
            rvSearch.setVisibility(View.VISIBLE);
        }
    }

    //点击事件
    @Override
    public void onClick(View v)
    {
        //Drawable drawable=getResources().getDrawable(R.drawable.textview_search_filter_click);
        switch (v.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                //先清空列表
                searchProgramList.clear();
                rvSearch.setVisibility(View.GONE);
                pbSearch.setVisibility(View.VISIBLE);
                //搜索加载
                String programName=etInput.getText().toString();
                String address="https://api.github.com/search/repositories?q="+programName;
                HttpUtil.sendOkHttpRequest(address, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        gsonPrase(result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pbSearch.setVisibility(View.GONE);
                                rvSearch.setVisibility(View.VISIBLE);
                                searchAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
    }

}
