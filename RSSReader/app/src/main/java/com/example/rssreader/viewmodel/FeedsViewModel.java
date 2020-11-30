package com.example.rssreader.viewmodel;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rssreader.data.Feed;
import com.example.rssreader.database.FeedDao;
import com.example.rssreader.repository.FeedRepository;
import com.example.rssreader.repository.RepositoryCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class FeedsViewModel extends ViewModel {

    private final FeedRepository feedRepository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final LiveData<List<Feed>> feeds;

    @ViewModelInject
    public FeedsViewModel(@NotNull FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
        this.feeds = feedRepository.getFeeds();
    }

    public LiveData<List<Feed>> getFeeds() {
        return feeds;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadFeeds(String url) {
        feedRepository.loadFeeds(url, new RepositoryCallback<List<Feed>>() {
            @Override
            public void onSuccess() {
                loading.postValue(false);
            }

            @Override
            public void onLoading() {
                loading.postValue(true);
            }

            @Override
            public void onError(String message) {
                loading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }
}