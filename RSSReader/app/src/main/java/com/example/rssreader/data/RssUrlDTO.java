package com.example.rssreader.data;

import pl.droidsonroids.jspoon.annotation.Selector;

public class RssUrlDTO {
    @Selector(value = "link[type=application/rss+xml]", attr ="href")
    public String url;
}
