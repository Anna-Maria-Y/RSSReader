package com.example.rssreader.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rssreader.data.FeedState;
import com.example.rssreader.data.dto.database.FeedDB;

import java.util.List;

@Dao
public interface FeedDao {

    @Query("UPDATE feeds SET state = :state WHERE url =:url")
    void updateFeed(int state, String url);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<FeedDB> feeds);

    @Query("SELECT * FROM feeds WHERE state != 2 ORDER BY state DESC, pubDate DESC")
    LiveData<List<FeedDB>> getFeeds();

    @Query("SELECT * FROM feeds WHERE state != 2 and title LIKE :query or description LIKE :query ORDER BY state DESC, pubDate DESC")
    LiveData<List<FeedDB>> getFeeds(String query);

}
