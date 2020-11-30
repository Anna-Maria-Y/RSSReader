package com.example.rssreader.viewmodel;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.ViewModel;

import com.example.rssreader.data.FeedState;
import com.example.rssreader.repository.FeedRepository;

public class WebViewViewModel extends ViewModel {

    private final FeedRepository feedRepository;

    @ViewModelInject
    public WebViewViewModel(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public void markFeedAsReading(String url){
        feedRepository.updateFeedState(FeedState.READING, url);
    }

    public void markFeedAsDone(String url){
        feedRepository.updateFeedState(FeedState.DONE, url);
    }

}