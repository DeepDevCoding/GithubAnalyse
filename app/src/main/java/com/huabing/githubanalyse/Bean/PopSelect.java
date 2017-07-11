package com.huabing.githubanalyse.Bean;

/**
 * Created by 30781 on 2017/7/2.
 */

public class PopSelect {
    private int imageId;
    private String name;
    public PopSelect(int imageId,String name)
    {
        this.imageId=imageId;
        this.name=name;
    }

    public int getImageId()
    {
        return imageId;
    }
    public String getName()
    {
        return name;
    }
}
