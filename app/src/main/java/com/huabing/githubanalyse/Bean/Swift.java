package com.huabing.githubanalyse.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 30781 on 2017/7/8.
 */

public class Swift extends DataSupport {
    private String user;       //作者
    private String type;   //语言
    private String program;    //项目名
    private String introduce;  //介绍
    private int collect;   //收藏
    private int star;          //星星数

    public void setUser(String user)
    {
        this.user=user;
    }
    public String getUser()
    {
        return user;
    }

    public void setType(String type)
    {
        this.type=type;
    }
    public String getType()
    {
        return type;
    }

    public void setProgram(String program)
    {
        this.program=program;
    }
    public String getProgram()
    {
        return program;
    }

    public void setIntroduce(String introduce)
    {
        this.introduce=introduce;
    }
    public String getIntroduce()
    {
        return introduce;
    }

    public void setCollect(int collect)
    {
        this.collect=collect;
    }
    public int getCollect()
    {
        return collect;
    }

    public void setStar(int star)
    {
        this.star=star;
    }
    public int getStar()
    {
        return star;
    }
}
