package com.example.rssreader.data.dto.network;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class FeedResponse {

    @Element(name = "title")
    private String title;

    @Element(name = "link")
    private String url;

    @Element(name = "description")
    private String description;

    @Element(name = "pubDate")
    private String pubDate;

    public String getUrl() {
        return url;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }
}
