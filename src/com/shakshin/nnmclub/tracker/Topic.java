package com.shakshin.nnmclub.tracker;

public class Topic {
    public Integer id;
    public String title;
    public String url;

    public Topic(Integer id, String title) {
        this.id = id;
        this.title = title;
        this.url = null;
    }

    @Override
    public String toString() {
        return id.toString() + ": " + title + (url == null ? "" : " (" + url + ")");
    }
}
