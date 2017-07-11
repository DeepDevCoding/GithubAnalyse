package com.huabing.githubanalyse.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 30781 on 2017/6/6.
 */

public class Pypl extends DataSupport{
    private String rank;
    private int change;
    private String type;
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
    public void setType(String type)
    {
        this.type=type;
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
    public String getType()
    {
        return type;
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
