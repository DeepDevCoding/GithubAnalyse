package com.huabing.githubanalyse;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huabing.githubanalyse.Adapter.HotPagerAdapter;
import com.huabing.githubanalyse.Util.HttpUtil;
import com.huabing.githubanalyse.View.PyplFragment;
import com.huabing.githubanalyse.View.TiobeFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Response;

public class HotFragment extends Fragment {

    private TabLayout tlPyplLayout;
    private ViewPager vpHotPager;
    private List<Fragment> fragmentList;
    private HotPagerAdapter adapter;

    public HotFragment() {

    }


    public static HotFragment newInstance() {
        HotFragment fragment = new HotFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_hot, container, false);
        String[] titles=new String[]{"总榜","趋势"};
        fragmentList=new ArrayList<>();
        fragmentList.add(TiobeFragment.newInstance());
        fragmentList.add(PyplFragment.newInstance());

        tlPyplLayout=(TabLayout)view.findViewById(R.id.tl_pypl_layout);
        for(String title:titles)
            tlPyplLayout.addTab(tlPyplLayout.newTab().setText(title));

        vpHotPager=(ViewPager)view.findViewById(R.id.vp_hot_pager);
        adapter=new HotPagerAdapter(getChildFragmentManager(),titles,fragmentList);
        vpHotPager.setAdapter(adapter);
        tlPyplLayout.setupWithViewPager(vpHotPager);



        return view;
    }

}
