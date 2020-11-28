package com.example.rssreader.di;

import android.app.Application;

import com.example.rssreader.network.RSSService;
import com.example.rssreader.repository.FeedRepository;
import com.example.rssreader.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@InstallIn(ApplicationComponent.class)
@Module
public final class AppModule {

    @Provides
    @Singleton
    static RSSService provideRSSService(){
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        return retrofit.create(RSSService.class);
    }

    @Provides
    @Singleton
    static FeedRepository provideFeedRepository(RSSService service){
        return new  FeedRepository(service);
    }


}
