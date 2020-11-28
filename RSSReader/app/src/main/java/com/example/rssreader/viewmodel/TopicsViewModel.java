package com.example.rssreader.viewmodel;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rssreader.data.Feed;
import com.example.rssreader.repository.FeedRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TopicsViewModel extends ViewModel {
    private final FeedRepository feedRepository;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private MutableLiveData<List<Feed>> feeds;

    @ViewModelInject
    public TopicsViewModel(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public MutableLiveData<List<Feed>> getFeeds() {
        if (feeds == null) {
            feeds = new MutableLiveData<List<Feed>>();
        }
        return feeds;
    }

    public void loadFeeds(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                feeds.postValue(feedRepository.getFeeds());
            }
        });
    }
}