package com.example.rssreader.repository;

import com.example.rssreader.data.Feed;
import com.example.rssreader.network.Result;

import java.util.List;

public interface RepositoryCallback<T> {
    void onLoading();
    void onComplete(Result<T> result);
}
