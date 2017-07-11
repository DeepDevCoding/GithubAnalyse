package com.huabing.githubanalyse.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 30781 on 2017/7/1.
 */

public class Ide extends DataSupport {
    private String rank;
    private int change;
    private String ide;
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
    public void setIde(String ide)
    {
        this.ide=ide;
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
    public String getIde()
    {
        return ide;
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
