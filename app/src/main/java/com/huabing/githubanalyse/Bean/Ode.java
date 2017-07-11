package com.huabing.githubanalyse.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 30781 on 2017/7/1.
 */

public class Ode extends DataSupport {
    private String rank;
    private int change;
    private String ode;
    private String share;
    private String trend;

    public void setRank(String rank)
    {
        this.rank=rank;
    }
    public void setChange(int change)
    {
        this.change=change;
    }
    public void setOde(String ode)
    {
        this.ode=ode;
    }
    public void setShare(String share)
    {
        this.share=share;
    }
    public void setTrend(String trend)
    {
        this.trend=trend;
    }

    public String getRank()
    {
        return rank;
    }
    public int getChange()
    {
        return change;
    }
    public String getOde()
    {
        return ode;
    }
    public String getShare()
    {
        return share;
    }
    public String getTrend()
    {
        return trend;
    }
}