package com.example.rssreader.data.dto.network;

import pl.droidsonroids.jspoon.annotation.Selector;

public class RssUrlResponse {
    @Selector(value = "link[type=application/rss+xml]", attr ="href")
    public String url;
}
