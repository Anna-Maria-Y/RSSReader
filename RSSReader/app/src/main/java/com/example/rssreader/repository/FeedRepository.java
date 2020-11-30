package com.example.rssreader.repository;

import com.example.rssreader.data.Feed;
import com.example.rssreader.data.FeedResponseDTO;
import com.example.rssreader.data.mapper.FeedMapper;
import com.example.rssreader.network.NoConnectivityException;
import com.example.rssreader.network.RssReaderService;
import com.example.rssreader.network.Result;

import org.jetbrains.annotations.NotNull;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedRepository {

    private final RssReaderService rssReaderService;

    private final ExecutorService executorService;

    @Inject
    public FeedRepository(RssReaderService rssReaderService, ExecutorService executorService) {
        this.rssReaderService = rssReaderService;
        this.executorService = executorService;
    }

    public void loadFeeds(String url, final RepositoryCallback<List<Feed>> callback){
        executorService.execute(() -> load(url, callback));
    }

    private void load(String url, final RepositoryCallback<List<Feed>> callback){
        callback.onLoading();
        final Call<FeedResponseDTO> call =  rssReaderService.getFeedResponse(url);
        call.enqueue(new Callback<FeedResponseDTO>() {
            @Override
            public void onResponse(@NotNull Call<FeedResponseDTO> call, @NotNull Response<FeedResponseDTO> response) {
                assert response.body() != null;
                List<Feed> feeds = FeedMapper.map(response.body().channel.feeds);
                callback.onComplete(new Result.Success<>(feeds));
            }

            @Override
            public void onFailure(@NotNull Call<FeedResponseDTO> call, @NotNull Throwable t) {
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
