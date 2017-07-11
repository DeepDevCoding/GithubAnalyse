package com.huabing.githubanalyse.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 30781 on 2017/6/7.
 */

public class Tiobe extends DataSupport{
    private String rank;
    private String oldRank;
    private int changeImage;
    private String language;
    private String ratings;
    private String change;

    public void setRank(String rank)
    {
        this.rank=rank;
    }
    public void setOldRank(String oldRank)
    {
        this.oldRank=oldRank;
    }
    public void setChangeImage(int changeImage){this.changeImage=changeImage;}
    public void setLanguage(String language)
    {
        this.language=language;
    }
    public void setRatings(String ratings)
    {
        this.ratings=ratings;
    }
    public void setChange(String change)
    {
        this.change=change;
    }


    public String getRank()
    {
        return rank;
    }
    public String getOldRank()
    {
        return oldRank;
    }
    public int getChangeImage(){return changeImage;}
    public String getLanguage()
    {
        return language;
    }
    public String getRatings()
    {
        return ratings;
    }
    public String getChange()
    {
        return change;
    }

}
