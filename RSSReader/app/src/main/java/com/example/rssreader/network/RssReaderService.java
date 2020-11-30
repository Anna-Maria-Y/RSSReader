package com.example.rssreader.network;

import com.example.rssreader.data.FeedResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RssReaderService {
    @GET
    Call<FeedResponseDTO> getFeedResponse(@Url String url);
}
