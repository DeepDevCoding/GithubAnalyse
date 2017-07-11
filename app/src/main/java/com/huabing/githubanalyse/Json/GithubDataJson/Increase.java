package com.huabing.githubanalyse.Json.GithubDataJson;

/**
 * Created by 30781 on 2017/7/8.
 */

public class Increase {
    private int year;
    private int user;
    private long repository;
    public void setYear(int year) {
        this.year = year;
    }
    public int getYear() {
        return year;
    }

    public void setUser(int user) {
        this.user = user;
    }
    public int getUser() {
        return user;
    }

    public void setRepository(long repository) {
        this.repository = repository;
    }
    public long getRepository() {
        return repository;
    }
}
