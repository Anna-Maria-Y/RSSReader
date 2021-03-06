package com.example.rssreader.network;

import com.example.rssreader.data.dto.network.RssUrlResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RssFinderService {
    @GET
    Call<RssUrlResponse> findRssUrl(@Url String url);

    @GET
    Call<Void> checkUrl(@Url String url);
}
