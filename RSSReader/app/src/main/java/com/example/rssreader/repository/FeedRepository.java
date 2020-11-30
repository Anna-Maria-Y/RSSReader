package com.example.rssreader.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.rssreader.data.Feed;
import com.example.rssreader.data.FeedState;
import com.example.rssreader.data.dto.database.FeedDB;
import com.example.rssreader.database.FeedDao;
import com.example.rssreader.database.FeedsDatabase;
import com.example.rssreader.data.dto.network.RssResponse;
import com.example.rssreader.data.mapper.FeedMapper;
import com.example.rssreader.network.NoConnectivityException;
import com.example.rssreader.network.RssReaderService;

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

    private final FeedDao feedDao;

    private final LiveData<List<Feed>> feeds;

    @Inject
    public FeedRepository(RssReaderService rssReaderService, ExecutorService executorService, FeedDao feedDao) {
        this.rssReaderService = rssReaderService;
        this.executorService = executorService;
        this.feedDao = feedDao;
        this.feeds = Transformations.map(feedDao.getFeedsAskPubDate(), FeedMapper::mapToFeedFromFeedDB);
    }

    public void loadFeeds(String url, final RepositoryCallback<List<Feed>> callback){
        executorService.execute(() -> load(url, callback));
    }

    private void load(String url, final RepositoryCallback<List<Feed>> callback){
        callback.onLoading();
        final Call<RssResponse> call =  rssReaderService.getFeedResponse(url);
        call.enqueue(new Callback<RssResponse>() {
            @Override
            public void onResponse(@NotNull Call<RssResponse> call, @NotNull Response<RssResponse> response) {
                assert response.body() != null;
                List<Feed> feeds = FeedMapper.mapToFeedFromResponse(response.body().channel.feeds);
                saveFeeds(feeds);
                callback.onSuccess();
            }

            @Override
            public void onFailure(@NotNull Call<RssResponse> call, @NotNull Throwable t) {
                String errorMessage;
                if(t instanceof NoConnectivityException){
                    errorMessage = "No network available, please check your WiFi or Data connection.";
                }else if(t instanceof SocketTimeoutException){
                    errorMessage = "Socket Time out. Please try again.";
                }
                else errorMessage = t.getLocalizedMessage();
                assert errorMessage != null : "Unknown Error";
                callback.onError(errorMessage);
            }
        });
    }

    public LiveData<List<Feed>> getFeeds(){
        return feeds;
    }

    private void saveFeeds(List<Feed> feeds) {
        FeedsDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                feedDao.insertAll(FeedMapper.mapToFeedDBFromFeed(feeds));
            }
        });
    }

    public void updateFeedState(FeedState state, String url) {
        FeedsDatabase.databaseWriteExecutor.execute(() -> feedDao.updateFeed(state.ordinal(), url));
    }


}
