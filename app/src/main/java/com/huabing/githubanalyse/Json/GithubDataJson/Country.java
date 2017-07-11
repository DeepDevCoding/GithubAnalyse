package com.huabing.githubanalyse.Json.GithubDataJson;

/**
 * Created by 30781 on 2017/7/8.
 */

public class Country {
    private String name;
    private double rate;
    private String image;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
    public double getRate() {
        return rate;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return image;
    }
}
