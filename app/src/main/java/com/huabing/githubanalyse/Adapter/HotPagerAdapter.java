package com.huabing.githubanalyse.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 30781 on 2017/6/5.
 */

public class HotPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private String[] titles;

    public HotPagerAdapter(FragmentManager manager, String[] titles, List<Fragment> fragmentList)
    {
        super(manager);
        this.titles=titles;
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int arg0)
    {
        return fragmentList.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return titles[position];
    }

    @Override
    public int getCount()
    {
        return fragmentList.size();
    }
}
