package com.huabing.githubanalyse.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 30781 on 2017/6/14.
 */

public class News extends DataSupport{
    private String name;
    private String content;
    private String style;
    private String star;

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
    public void setStyle(String style)
    {
        this.style=style;
    }
    public String getStyle()
    {
        return style;
    }
    public void setStar(String star)
    {
        this.star=star;
    }
    public String getStar()
    {
        return star;
    }
}
