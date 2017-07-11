package com.huabing.githubanalyse.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 30781 on 2017/6/3.
 */

public class Mine extends DataSupport{
    private String account;
    private String password;
    private String picUrl;
    private String programUrl;
    private String blog;
    private String createTime;

    public void setAccount(String account)
    {
        this.account=account;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }
    public void setPicUrl(String picUrl)
    {
        this.picUrl=picUrl;
    }
    public void setProgramUrl(String programUrl)
    {
        this.programUrl=programUrl;
    }
    public void setBlog(String blog)
    {
        this.blog=blog;
    }
    public void setCreateTime(String createTime)
    {
        this.createTime=createTime;
    }

    public String getAccount()
    {
        return account;
    }
    public String getPassword()
    {
        return password;
    }
    public String getPicUrl()
    {
        return picUrl;
    }
    public String getProgramUrl()
    {
        return programUrl;
    }
    public String getBlog()
    {
        return blog;
    }
    public String getCreateTime()
    {
        return createTime;
    }
}
