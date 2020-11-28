package com.example.rssreader.repository;

import android.os.AsyncTask;

import com.example.rssreader.data.Feed;
import com.example.rssreader.data.FeedResponse;
import com.example.rssreader.data.mapper.FeedMapper;
import com.example.rssreader.network.RSSService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;

public class FeedRepository {

    private final RSSService service;

    @Inject
    public FeedRepository(RSSService service) {
        this.service = service;
    }

    public List<Feed> getFeeds(){
        Call<FeedResponse> call =  service.getFeedResponse("https://news.tut.by/rss/all.rss");
        FeedResponse response = null;
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FeedMapper.map(response.channel.feeds);
    }
}
