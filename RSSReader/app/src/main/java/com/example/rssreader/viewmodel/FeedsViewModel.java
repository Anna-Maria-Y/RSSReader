package com.example.rssreader.viewmodel;

import androidx.arch.core.util.Function;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.rssreader.data.Feed;
import com.example.rssreader.data.mapper.FeedMapper;
import com.example.rssreader.database.FeedDao;
import com.example.rssreader.repository.FeedRepository;
import com.example.rssreader.repository.RepositoryCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;


public class FeedsViewModel extends ViewModel {

    private final FeedRepository feedRepository;

    private final LiveData<List<Feed>> feeds;

    private final MutableLiveData<String> query = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @ViewModelInject
    public FeedsViewModel(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
        this.feeds = Transformations.map(feedRepository.getFeeds(), FeedMapper::mapToFeedFromFeedDB);

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

    public void searchFeeds(String query){
        feedRepository.searchFeeds("%" + query.replace(' ', '%') + "%");
    }
}