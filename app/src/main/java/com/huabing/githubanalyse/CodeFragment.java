package com.huabing.githubanalyse;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huabing.githubanalyse.Adapter.CodePagerAdapter;
import com.huabing.githubanalyse.View.AllLanguageFragment;
import com.huabing.githubanalyse.View.CplusFragment;
import com.huabing.githubanalyse.View.CFragment;
import com.huabing.githubanalyse.View.JavaFragment;
import com.huabing.githubanalyse.View.JavaScriptFragment;
import com.huabing.githubanalyse.View.PhpFragment;
import com.huabing.githubanalyse.View.PythonFragment;
import com.huabing.githubanalyse.View.RubyFragment;

import java.util.ArrayList;
import java.util.List;

public class CodeFragment extends Fragment {

    private TabLayout tlStyle;
    private ViewPager vpPager;
    private FragmentManager manager;
    private List<Fragment> fragmentList;
    private CodePagerAdapter adapter;

    public CodeFragment() {
    }

    public static CodeFragment newInstance() {
        CodeFragment fragment = new CodeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_code, container, false);
        //TabLayout初始化
        tlStyle=(TabLayout)view.findViewById(R.id.tl_style);
        tlStyle.setTabMode(TabLayout.MODE_SCROLLABLE);
        String[] titles=new String[]{"All","C","C++","Java","JavaScript","Python","Swift","PHP","Ruby"};
        for(String title:titles)
            tlStyle.addTab(tlStyle.newTab().setText(title));

        //编程语言Fragment
        fragmentList=new ArrayList<>();
        fragmentList.add(AllLanguageFragment.newInstance());
        fragmentList.add(CFragment.newInstance());
        fragmentList.add(CplusFragment.newInstance());
        fragmentList.add(JavaFragment.newInstance());
        fragmentList.add(JavaScriptFragment.newInstance());
        fragmentList.add(PythonFragment.newInstance());
        fragmentList.add(SwiftFragment.newInstance());
        fragmentList.add(PhpFragment.newInstance());
        fragmentList.add(RubyFragment.newInstance());

        //初始化ViewPager
        vpPager=(ViewPager)view.findViewById(R.id.vpPager);
        manager=getChildFragmentManager();
        adapter=new CodePagerAdapter(manager,titles,fragmentList);
        vpPager.setAdapter(adapter);

        //把Viewpager加入到Tablayout中
        tlStyle.setupWithViewPager(vpPager);

        return view;
    }

}
