package com.huabing.githubanalyse.Bean;

/**
 * Created by 30781 on 2017/7/3.
 */

public class Country {
    private String name;
    private double increase;
    private String imageUrl;

    public Country(String name,double increase,String imageUrl)
    {
        this.name=name;
        this.increase=increase;
        this.imageUrl=imageUrl;
    }

    public String getName()
    {
        return name;
    }
    public double getIncrease()
    {
        return increase;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

}
