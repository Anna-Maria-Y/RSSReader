package com.example.rssreader.repository;

import android.accounts.NetworkErrorException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rssreader.data.Feed;
import com.example.rssreader.data.FeedResponse;
import com.example.rssreader.data.mapper.FeedMapper;
import com.example.rssreader.network.NoConnectivityException;
import com.example.rssreader.network.RSSService;
import com.example.rssreader.network.Result;

import org.jetbrains.annotations.NotNull;

import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeedRepository {

    private final RSSService rssService;

    private final ExecutorService executorService;

    @Inject
    public FeedRepository(RSSService rssService, ExecutorService executorService) {
        this.rssService = rssService;
        this.executorService = executorService;
    }

    public void loadFeeds(final RepositoryCallback<List<Feed>> callback){
        executorService.execute(() -> load(callback));
    }

    private void load( final RepositoryCallback<List<Feed>> callback){
        callback.onLoading();
        final Call<FeedResponse> call =  rssService.getFeedResponse("https://news.tut.by/rss/all.rss");
        call.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(@NotNull Call<FeedResponse> call, @NotNull Response<FeedResponse> response) {
                assert response.body() != null;
                List<Feed> feeds = FeedMapper.map(response.body().channel.feeds);
                callback.onComplete(new Result.Success<>(feeds));
            }

            @Override
            public void onFailure(@NotNull Call<FeedResponse> call, @NotNull Throwable t) {
                String errorMessage;
                if(t instanceof NoConnectivityException){
                    errorMessage = "No network available, please check your WiFi or Data connection.";
                }else if(t instanceof SocketTimeoutException){
                    errorMessage = "Socket Time out. Please try again.";
                }
                else errorMessage = t.getLocalizedMessage();
                assert errorMessage != null : "Unknown Error";
                callback.onComplete(new Result.Error<>(null, errorMessage));
            }
        });
    }
}
