package io.hefuyi.zhihudaily.mvp.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Aspsine on 2015/3/19.
 */
public class Themes {
    @Expose
    private int limit;
    @Expose
    private List<Theme> subscribed;
    @Expose
    private List<Theme> others;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Theme> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(List<Theme> subscribed) {
        this.subscribed = subscribed;
    }

    public List<Theme> getOthers() {
        return others;
    }

    public void setOthers(List<Theme> others) {
        this.others = others;
    }
}
