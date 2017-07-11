package com.huabing.githubanalyse.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;;

import java.util.List;

/**
 * Created by 30781 on 2017/5/25.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragmentList;

    public MainPagerAdapter(FragmentManager manager, List<Fragment> fragmentList)
    {
        super(manager);
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int arg0)
    {
        return fragmentList.get(arg0);
    }

    @Override
    public int getCount()
    {
        return fragmentList.size();
    }

}
