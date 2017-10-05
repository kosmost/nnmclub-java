package com.shakshin.nnmclub;

public class TopicCfg {
    Integer id;
    String title;
    String lastUpdated;

    public Integer getId() {
        return id;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return id.toString() + ": " + (title != null && !title.isEmpty() ? title : "No title fetched" ) + "\n" +
                (lastUpdated != null && !lastUpdated.isEmpty() ? "  Last updated at: " + lastUpdated : "  Never updated yet");
    }
}
