package io.hefuyi.zhihudaily.mvp.model;

import com.google.gson.annotations.Expose;

/**
 * Created by hefuyi on 16/7/30.
 */
public class StartImage {
    @Expose
    private String text;
    @Expose
    private String img;

    public void setText(String text) {
        this.text = text;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public String getText() {
        return text;
    }
}
