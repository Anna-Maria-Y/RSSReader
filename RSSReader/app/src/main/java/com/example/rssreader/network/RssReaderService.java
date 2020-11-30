package com.example.rssreader.network;

import com.example.rssreader.data.dto.network.RssResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RssReaderService {
    @GET
    Call<RssResponse> getFeedResponse(@Url String url);
}
