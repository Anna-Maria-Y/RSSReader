package com.example.rssreader.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "rss", strict = false)
public class FeedResponse {

    @Element(name = "channel")
    public Channel channel;

    @Root(name = "channel", strict = false)
    public static class Channel {
        @ElementList(entry = "item", inline = true)
        public List<FeedDTO> feeds;
    }
}
