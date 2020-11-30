package com.example.rssreader.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rssreader.data.RssUrlDTO;
import com.example.rssreader.network.Result;
import com.example.rssreader.network.RssFinderService;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsViewModel extends ViewModel {
    private final RssFinderService rssFinderService;

    private final ExecutorService executorService;

    private final MutableLiveData<Result<String>> rssUrl = new MutableLiveData<>();

    public MutableLiveData<Result<String>> getRssUrl() {
        return rssUrl;
    }

    @ViewModelInject
    public SettingsViewModel(RssFinderService rssFinderService, ExecutorService executorService) {
        this.rssFinderService = rssFinderService;
        this.executorService = executorService;
    }

    public void checkUrl(String url){
        Call<Void> call = rssFinderService.checkUrl(url);
        executorService.execute(() -> callCheckUrl(call, url));
    }

    private void callCheckUrl(Call<Void> call, String url){
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if(response.headers().get("content-type").contains("xml")){
                    Log.i("Check RSS url", "OK! It's RSS.");
                    postSuccess(url);
                } else {
                    Log.i("Check RSS url", "Oh no! It isn't RSS.");
                    findRssUrl(url);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                postError(t.getLocalizedMessage());
            }
        });
    }

    private void findRssUrl(String url){
        Call<RssUrlDTO> call = rssFinderService.findRssUrl(url);
        executorService.execute(() -> callFindRssUrl(call));
    }

    private void callFindRssUrl(Call<RssUrlDTO> call){
        call.enqueue(new Callback<RssUrlDTO>() {
            @Override
            public void onResponse(Call<RssUrlDTO> call, Response<RssUrlDTO> response) {
                String url = response.body().url;
                if (url!=null){
                    postSuccess(url);
                } else postError("Site doesn't have RSS link.");
            }

            @Override
            public void onFailure(Call<RssUrlDTO> call, Throwable t) {
                postError( t.getLocalizedMessage());
            }
        });
    }

    private void postError(String errorMessage){
        rssUrl.postValue(new Result.Error<>(rssUrl.getValue().getData(), errorMessage));
    }

    private void postSuccess(String url){
        rssUrl.postValue(new Result.Success<>(url));
    }
}