package com.example.rssreader.di;

import android.content.Context;

import com.example.rssreader.database.FeedDao;
import com.example.rssreader.database.FeedsDatabase;
import com.example.rssreader.network.ConnectivityInterceptor;
import com.example.rssreader.network.RssFinderService;
import com.example.rssreader.network.RssReaderService;
import com.example.rssreader.repository.FeedRepository;

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
import pl.droidsonroids.retrofit2.JspoonConverterFactory;
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
    static FeedsDatabase provideDatabase(@ApplicationContext Context context){
        return FeedsDatabase.getDatabase(context);
    }


    @Provides
    @Singleton
    static FeedDao provideFeedDao(FeedsDatabase database){
        return database.feedDao();
    }

    @Provides
    @Singleton
    static ExecutorService provideExecutorService(){
        return Executors.newFixedThreadPool ( 1 );
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient(ConnectivityInterceptor interceptor){
        return new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }



    @Provides
    @Singleton
    static RssReaderService provideRssReaderService(OkHttpClient okHttpClient){
        final Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://localhost/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        return retrofit.create(RssReaderService.class);
    }

    @Provides
    @Singleton
    static RssFinderService provideRssFinderService(OkHttpClient okHttpClient){
        final Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://localhost/")
                .addConverterFactory(JspoonConverterFactory.create())
                .build();
        return retrofit.create(RssFinderService.class);
    }

    @Provides
    @Singleton
    static FeedRepository provideFeedRepository(RssReaderService rssReaderService, ExecutorService executorService, FeedDao feedDao){
        return new  FeedRepository(rssReaderService, executorService, feedDao);
    }
}
