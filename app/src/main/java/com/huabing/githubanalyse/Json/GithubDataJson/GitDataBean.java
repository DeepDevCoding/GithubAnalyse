package com.huabing.githubanalyse.Json.GithubDataJson;

import java.util.List;

/**
 * Created by 30781 on 2017/7/8.
 */

public class GitDataBean {
    private List<Language> language;
    private List<Sex> sex;
    private List<Country> country;
    private List<Organization> organization;
    private List<Increase> increase;
    public void setLanguage(List<Language> language) {
        this.language = language;
    }
    public List<Language> getLanguage() {
        return language;
    }

    public void setSex(List<Sex> sex) {
        this.sex = sex;
    }
    public List<Sex> getSex() {
        return sex;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }
    public List<Country> getCountry() {
        return country;
    }

    public void setOrganization(List<Organization> organization) {
        this.organization = organization;
    }
    public List<Organization> getOrganization() {
        return organization;
    }

    public void setIncrease(List<Increase> increase) {
        this.increase = increase;
    }
    public List<Increase> getIncrease() {
        return increase;
    }
}
