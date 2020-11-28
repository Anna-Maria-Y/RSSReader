package com.example.rssreader.network;

import com.example.rssreader.data.FeedResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RSSService {
    @GET
    Call<FeedResponse> getFeedResponse(@Url String url);
}
