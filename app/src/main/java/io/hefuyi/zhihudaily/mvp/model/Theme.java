package io.hefuyi.zhihudaily.mvp.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Aspsine on 2015/3/19.
 */
public class Theme {
    @Expose
    private String color;
    @Expose
    private String thumbnail;
    @Expose
    private String image;
    @Expose
    private String background;
    @Expose
    private String description;
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private List<Story> stories;
    @Expose
    private List<Editor> editors;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Editor> getEditors() {
        return editors;
    }

    public void setEditors(List<Editor> editors) {
        this.editors = editors;
    }
}
