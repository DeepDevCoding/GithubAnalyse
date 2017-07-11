package com.huabing.githubanalyse;


import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.huabing.githubanalyse.Adapter.PopSelectAdapter;
import com.huabing.githubanalyse.Adapter.UserCountryAdapter;
import com.huabing.githubanalyse.Bean.Country;
import com.huabing.githubanalyse.Bean.PopSelect;
import com.huabing.githubanalyse.Json.GithubDataJson.GitDataBean;
import com.huabing.githubanalyse.Util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserFragment extends Fragment implements View.OnClickListener{

    private LinearLayout llLayout;
    private TextView tvSort;
    private ImageView ivSelect;

    private PopupWindow popWindow;
    private List<PopSelect> popList;
    private PopSelectAdapter popAdapter;
    private ListView popListView;

    private LinearLayout llCountryState;
    private List<Country> countryList;
    private UserCountryAdapter countryAdapter;
    private RecyclerView rvUserCountry;

    private HorizontalBarChart hbcLanguage;
    private PieChart pcSex;
    private BarChart bcOrganization;

    private LinearLayout llUserProgram;
    private LineChart lcUserProgram;

    public UserFragment() {
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user, container, false);
        //初始化选择列表
        llLayout=(LinearLayout)view.findViewById(R.id.ll_layout);
        llLayout.setOnClickListener(this);
        tvSort=(TextView)view.findViewById(R.id.tv_sort);
        ivSelect=(ImageView)view.findViewById(R.id.iv_select);
        //初始化PopWindow
        initPopWindow();

        //语言条形图
        hbcLanguage=(HorizontalBarChart)view.findViewById(R.id.hbc_language);
        initLanguageChart();

        //性别饼状图
        pcSex=(PieChart)view.findViewById(R.id.pc_sex);

        //国家地区增长图
        llCountryState=(LinearLayout)view.findViewById(R.id.ll_user_country_state);
        countryList=new ArrayList<>();
        rvUserCountry=(RecyclerView)view.findViewById(R.id.rv_user_country);
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rvUserCountry.setLayoutManager(manager);
        countryAdapter=new UserCountryAdapter(countryList);
        rvUserCountry.setAdapter(countryAdapter);

        //语言条形图
        bcOrganization=(BarChart)view.findViewById(R.id.bc_organization);
        initOrganizeChart();

        //Github增长折线图
        llUserProgram=(LinearLayout)view.findViewById(R.id.ll_user_program);
        lcUserProgram=(LineChart)view.findViewById(R.id.lc_user_program);

        return view;
    }

    //初始化PopWindow
    private void initPopWindow()
    {
        View popView = View.inflate(getActivity(), R.layout.popwindow_layout, null);
        popWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setFocusable(true);
        popWindow.setAnimationStyle(R.style.popwindow_anim_style);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            public void onDismiss()
            {
                tvSort.setTextColor(Color.parseColor("#5a5959"));
            }
        });

        //初始化PopWindow中的ListView
        popList=new ArrayList<>();
        PopSelect oneSelect=new PopSelect(R.drawable.user_select_language,"语言");
        popList.add(oneSelect);
        PopSelect twoSelect=new PopSelect(R.drawable.user_select_sex,"性别");
        popList.add(twoSelect);
        PopSelect threeSelect=new PopSelect(R.drawable.user_select_country,"地区");
        popList.add(threeSelect);
        PopSelect fourSelect=new PopSelect(R.drawable.user_select_organization,"组织");
        popList.add(fourSelect);
        PopSelect increaseSelect=new PopSelect(R.drawable.user_select_increase,"增长");
        popList.add(increaseSelect);
        popAdapter=new PopSelectAdapter(getActivity(),R.layout.popwindow_item,popList);
        popListView=(ListView)popView.findViewById(R.id.pop_list);
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popWindow.dismiss();
                ivSelect.setImageResource(R.drawable.user_select_up);
                switch (position)
                {
                    //语言
                    case 0:
                        //隐藏
                        pcSex.setVisibility(View.GONE);
                        llCountryState.setVisibility(View.GONE);
                        bcOrganization.setVisibility(View.GONE);
                        llUserProgram.setVisibility(View.GONE);
                        //显示
                        hbcLanguage.setVisibility(View.VISIBLE);
                        initLanguageChart();
                        break;
                    //性别
                    case 1:
                        //隐藏
                        hbcLanguage.setVisibility(View.GONE);
                        llCountryState.setVisibility(View.GONE);
                        bcOrganization.setVisibility(View.GONE);
                        llUserProgram.setVisibility(View.GONE);
                        //显示
                        pcSex.setVisibility(View.VISIBLE);
                        initSexChart();
                        break;
                    //地区
                    case 2:
                        //隐藏
                        hbcLanguage.setVisibility(View.GONE);
                        pcSex.setVisibility(View.GONE);
                        bcOrganization.setVisibility(View.GONE);
                        llUserProgram.setVisibility(View.GONE);
                        //显示
                        llCountryState.setVisibility(View.VISIBLE);
                        initCountryRecyclerView();
                        llCountryState.setVisibility(View.VISIBLE);
                        break;
                    //组织
                    case 3:
                        //隐藏
                        hbcLanguage.setVisibility(View.GONE);
                        pcSex.setVisibility(View.GONE);
                        llCountryState.setVisibility(View.GONE);
                        llUserProgram.setVisibility(View.GONE);
                        //显示
                        bcOrganization.setVisibility(View.VISIBLE);
                        initOrganizeChart();
                        break;
                    //用户、数目
                    case 4:
                        //隐藏
                        hbcLanguage.setVisibility(View.GONE);
                        pcSex.setVisibility(View.GONE);
                        llCountryState.setVisibility(View.GONE);
                        bcOrganization.setVisibility(View.GONE);
                        //显示
                        llUserProgram.setVisibility(View.VISIBLE);
                        initGithubIncrease();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //初始化语言条形图
    private void initLanguageChart()
    {
        //获取数据
        String address="http://123.207.38.178/githubuser.json";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result=response.body().string();
                if(result==null)
                {
                    Toast.makeText(getActivity(),"获取数据失败！",Toast.LENGTH_SHORT).show();
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //解析json数据
                        Gson gson=new Gson();
                        GitDataBean gitDataBean=gson.fromJson(result,GitDataBean.class);
                        //条形图设置
                        hbcLanguage.setDescription("");  //说明
                        hbcLanguage.animateX(1000);  //动画
                        hbcLanguage.setTouchEnabled(true);  //可触摸
                        hbcLanguage.setScaleEnabled(true);  //可缩放
                        hbcLanguage.setDragEnabled(true);  //可拖拽
                        //设置比例图标示
                        Legend mLegend=hbcLanguage.getLegend();

                        mLegend.setFormSize(6f);
                        mLegend.setTextColor(Color.RED);
                        //X轴位置
                        XAxis xAxis=hbcLanguage.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setTextSize(7f);  //设置X轴字体大小
                        xAxis.setDrawGridLines(false);
                        //X轴数据
                        ArrayList<String> xValue=new ArrayList<>();
                        int length=gitDataBean.getLanguage().size();
                        for(int i=0;i<length;i++)
                            xValue.add(gitDataBean.getLanguage().get(length-1-i).getName());

                        //Y轴数据
                        ArrayList<BarEntry> yValue=new ArrayList<>();
                        for(int i=0;i<length;i++)
                            yValue.add(new BarEntry(gitDataBean.getLanguage().get(i).getNum(),length-1-i));

                        //Y轴数据集
                        BarDataSet dataSet=new BarDataSet(yValue,"star项目语言统计");
                        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                        //加入X轴数据
                        BarData barData=new BarData(xValue,dataSet);
                        hbcLanguage.setData(barData);
                    }
                });
            }
        });


    }

    //初始化性别饼状图
    private void initSexChart()
    {
        //获取数据
        String address="http://123.207.38.178/githubuser.json";
        HttpUtil.sendOkHttpRequest(address, new Callback()
        {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result = response.body().string();
                        if (result == null) {
                            Toast.makeText(getActivity(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //解析json数据
                                Gson gson=new Gson();
                                GitDataBean gitDataBean=gson.fromJson(result,GitDataBean.class);
                                //饼状图
                                pcSex.setDescription("");
                                pcSex.setUsePercentValues(true); //显示百分比
                                pcSex.setCenterText("男女性别比例");  //居中说明
                                pcSex.animateXY(2000,1000);  //动画
                                //X、Y轴数据
                                ArrayList<String> xValue=new ArrayList<>();
                                ArrayList<Entry> yValue=new ArrayList<>();
                                int length=gitDataBean.getSex().size();
                                for(int i=0;i<length;i++) {
                                    xValue.add(gitDataBean.getSex().get(i).getName());
                                }
                                for(int i=0;i<length;i++) {
                                    float rate=(float)gitDataBean.getSex().get(i).getRate();
                                    yValue.add(new Entry(rate,i));
                                }
                                //数据集
                                PieDataSet dataSet=new PieDataSet(yValue,"比例图");
                                dataSet.setSliceSpace(5);

                                //设置颜色
                                ArrayList<Integer> colors=new ArrayList<>();
                                colors.add(Color.rgb(0,255,255));
                                colors.add(Color.rgb(255,132,255));
                                colors.add(Color.rgb(243,249,10));
                                dataSet.setColors(colors);

                                //加入X轴数据
                                PieData pieData=new PieData(xValue,dataSet);
                                pcSex.setData(pieData);
                            }
                        });
                    }
        });


    }

    //初始化国家地区RecyclerView
    private void initCountryRecyclerView()
    {
        //获取数据
        String address="http://123.207.38.178/githubuser.json";
        HttpUtil.sendOkHttpRequest(address, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                if (result == null) {
                    Toast.makeText(getActivity(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //解析json数据
                        Gson gson = new Gson();
                        GitDataBean gitDataBean = gson.fromJson(result, GitDataBean.class);
                        int length = gitDataBean.getCountry().size();
                        countryList.clear();
                        for(int i=0;i<length;i++) {
                            String name=gitDataBean.getCountry().get(i).getName();
                            double rate=(double) gitDataBean.getCountry().get(i).getRate();
                            String imageUrl=gitDataBean.getCountry().get(i).getImage();
                            countryList.add(new Country(name,rate,imageUrl));
                        }
                        countryAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    //初始化语言条形图
    private void initOrganizeChart()
    {
        //获取数据
        String address="http://123.207.38.178/githubuser.json";
        HttpUtil.sendOkHttpRequest(address, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                if (result == null) {
                    Toast.makeText(getActivity(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //解析json数据
                        Gson gson = new Gson();
                        GitDataBean gitDataBean = gson.fromJson(result, GitDataBean.class);
                        int length = gitDataBean.getOrganization().size();
                        //条形图设置
                        bcOrganization.setDescription("");  //说明
                        bcOrganization.animateX(1000);  //动画
                        bcOrganization.setTouchEnabled(true);  //可触摸
                        bcOrganization.setScaleEnabled(true);  //可缩放
                        bcOrganization.setDragEnabled(true);  //可拖拽
                        //设置比例图标示
                        Legend mLegend=hbcLanguage.getLegend();
                        mLegend.setFormSize(6f);
                        mLegend.setTextColor(Color.RED);
                        //X轴位置
                        XAxis xAxis=bcOrganization.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setTextSize(4f);  //设置X轴字体大小
                        xAxis.setDrawGridLines(false);
                        //X、Y轴数据
                        ArrayList<String> xValue=new ArrayList<>();
                        ArrayList<BarEntry> yValue=new ArrayList<>();
                        for(int i=0;i<length;i++)
                        {
                            xValue.add(gitDataBean.getOrganization().get(i).getName());
                            yValue.add(new BarEntry(gitDataBean.getOrganization().get(i).getNum(),i));
                        }
                        //隐藏右边的Y轴
                        bcOrganization.getAxisRight().setEnabled(false);
                        //Y轴数据集
                        BarDataSet dataSet=new BarDataSet(yValue,"GitHub最开源的组织前八");
                        //dataSet.setColor(Color.rgb(89,48,104));
                        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

                        //加入X轴数据
                        BarData barData=new BarData(xValue,dataSet);
                        bcOrganization.setData(barData);
                    }
                });
            }
        });

    }

    //初始化Github增长折线图
    private void initGithubIncrease()
    {
        //获取数据
        String address="http://123.207.38.178/githubuser.json";
        HttpUtil.sendOkHttpRequest(address, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                if (result == null) {
                    Toast.makeText(getActivity(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //解析json数据
                        Gson gson = new Gson();
                        GitDataBean gitDataBean = gson.fromJson(result, GitDataBean.class);
                        int length = gitDataBean.getIncrease().size();
                        //设置动画
                        lcUserProgram.animateXY(2000,1000);
                        //X轴数据
                        XAxis xAxis=lcUserProgram.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        ArrayList<String> xValues=new ArrayList<>();
                        ArrayList<Entry> yUserValue=new ArrayList<>();
                        ArrayList<Entry> yProgramValue=new ArrayList<>();
                        for(int i=0;i<length;i++)
                        {
                            xValues.add(""+gitDataBean.getIncrease().get(i).getYear());
                            yUserValue.add(new Entry(gitDataBean.getIncrease().get(i).getUser(),i));
                            yProgramValue.add(new Entry(gitDataBean.getIncrease().get(i).getRepository(),i));
                        }
                        LineDataSet userDataSet=new LineDataSet(yUserValue,"用户数");
                        LineDataSet programDataSet=new LineDataSet(yProgramValue,"项目数");
                        programDataSet.setColor(Color.RED);
                        //隐藏右Y轴
                        lcUserProgram.getAxisRight().setEnabled(false);
                        //加入两条折线
                        ArrayList<LineDataSet> dataSets=new ArrayList<>();
                        dataSets.add(userDataSet);
                        dataSets.add(programDataSet);
                        LineData lineData=new LineData(xValues,dataSets);
                        lcUserProgram.setData(lineData);
                    }
                });
            }
        });
    }

    //点击事件
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ll_layout:
                tvSort.setTextColor(Color.parseColor("#39ac69"));
                ivSelect.setImageResource(R.drawable.user_select_down);
                popListView.setAdapter(popAdapter);
                popWindow.showAsDropDown(llLayout);
                break;
        }
    }

}
