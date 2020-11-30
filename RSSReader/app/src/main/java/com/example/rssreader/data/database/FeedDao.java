package com.example.rssreader.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.rssreader.data.Feed;
import com.example.rssreader.data.dto.database.FeedDB;

import java.util.List;

@Dao
public interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FeedDB feed);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FeedDB> feeds);

    @Query("SELECT * FROM feeds ORDER BY pubDate DESC")
    LiveData<List<FeedDB>> getFeedsAskPubDate();
}
