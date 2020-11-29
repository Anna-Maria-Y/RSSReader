package com.example.rssreader.di;

import android.app.Application;
import android.content.Context;

import com.example.rssreader.network.ConnectivityInterceptor;
import com.example.rssreader.network.RSSService;
import com.example.rssreader.repository.FeedRepository;
import com.example.rssreader.ui.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@InstallIn(ApplicationComponent.class)
@Module
public final class AppModule {

    @Provides
    @Singleton
    static ConnectivityInterceptor provideConnectivityInterceptor(@ApplicationContext Context context){
        return new ConnectivityInterceptor(context);
    }

    @Provides
    @Singleton
    static ExecutorService provideExecutorService(){
        return Executors.newFixedThreadPool ( 1 );
    }

    @Provides
    @Singleton
    static RSSService provideRSSService(ConnectivityInterceptor interceptor){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        final Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://localhost/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        return retrofit.create(RSSService.class);
    }

    @Provides
    @Singleton
    static FeedRepository provideFeedRepository(RSSService rssService, ExecutorService executorService){
        return new  FeedRepository(rssService, executorService);
    }
}
