package com.huabing.githubanalyse.Bean;

/**
 * Created by 30781 on 2017/7/4.
 */

public class SearchProgram {
    private String name;
    private String content;
    private String update;
    private String language;
    private String user;

    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }

    public void setContent(String content)
    {
        this.content=content;
    }
    public String getContent()
    {
        return content;
    }
    public void setUpdate(String update)
    {
        this.update=update;
    }
    public String getUpdate()
    {
        return update;
    }
    public void setLanguage(String language)
    {
        this.language=language;
    }
    public String getLanguage()
    {
        return language;
    }
    public void setUser(String user)
    {
        this.user=user;
    }
    public String getUser()
    {
        return user;
    }
}
