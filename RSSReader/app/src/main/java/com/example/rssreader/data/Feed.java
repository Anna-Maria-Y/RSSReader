package com.example.rssreader.data;

import java.time.ZonedDateTime;
import java.util.Calendar;

public class Feed {
    private String url;
    private String title;
    private String description;
    private ZonedDateTime pubDate;

    public Feed(String url, String title, String description, ZonedDateTime pubDate) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getPubDate() {
        return pubDate;
    }

    public void setPubDate(ZonedDateTime pubDate) {
        this.pubDate = pubDate;
    }
}
