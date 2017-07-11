package com.huabing.githubanalyse.Json.SearchJson;

import java.util.List;

/**
 * Created by 30781 on 2017/7/8.
 */

public class SearchBean {
    private int total_count;
    private boolean incomplete_results;
    private List<Items> items;
    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
    public int getTotal_count() {
        return total_count;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }
    public boolean getIncomplete_results() {
        return incomplete_results;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
    public List<Items> getItems() {
        return items;
    }
}
