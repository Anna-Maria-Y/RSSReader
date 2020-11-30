package com.example.rssreader.data.dto.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "feeds")
public class FeedDB {

    @PrimaryKey
    @NonNull
    private final String url;
    private final String title;
    private final String description;
    private final long pubDate;
    private final int state;

    public FeedDB(@NonNull String url, String title, String description, long pubDate, int state) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.state = state;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getPubDate() {
        return pubDate;
    }

    public int getState() {
        return state;
    }
}
