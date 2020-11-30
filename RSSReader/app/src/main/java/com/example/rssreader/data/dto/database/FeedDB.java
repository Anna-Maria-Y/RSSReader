package com.example.rssreader.data.dto.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "feeds")
public class FeedDB {

    @PrimaryKey
    @NonNull
    private String url;
    private String title;
    private String description;
    private Long pubDate;

    public FeedDB(@NonNull String url, String title, String description, Long pubDate) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
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

    public Long getPubDate() {
        return pubDate;
    }

}
