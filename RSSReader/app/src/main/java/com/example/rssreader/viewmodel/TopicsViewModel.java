package com.example.rssreader.viewmodel;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rssreader.data.Feed;
import com.example.rssreader.network.Result;
import com.example.rssreader.repository.FeedRepository;
import com.example.rssreader.repository.RepositoryCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class TopicsViewModel extends ViewModel {

    private final FeedRepository feedRepository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private final MutableLiveData<Result<List<Feed>>> result = new MutableLiveData<>();

    @ViewModelInject
    public TopicsViewModel(@NotNull FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public MutableLiveData<Result<List<Feed>>> getFeeds() {
        return result;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public void loadFeeds() {
        feedRepository.loadFeeds(new RepositoryCallback<List<Feed>>() {
            @Override
            public void onComplete(Result<List<Feed>> result) {
                loading.postValue(false);
                TopicsViewModel.this.result.postValue(result);
            }

            @Override
            public void onLoading() {
                loading.postValue(true);
            }
        });
    }
}