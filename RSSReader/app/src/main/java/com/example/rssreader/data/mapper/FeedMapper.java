package com.example.rssreader.data.mapper;

import com.example.rssreader.data.Feed;
import com.example.rssreader.data.FeedState;
import com.example.rssreader.data.dto.database.FeedDB;
import com.example.rssreader.data.dto.network.FeedResponse;
import com.example.rssreader.data.converter.DateConverter;

import java.util.List;
import java.util.stream.Collectors;

public class FeedMapper {

    public static List<Feed> mapToFeedFromResponse(List<FeedResponse> input) {
        return input.stream().map(FeedMapper::mapFeed).collect(Collectors.toList());
    }

    public static List<Feed> mapToFeedFromFeedDB(List<FeedDB> input) {
        return input.stream().map(FeedMapper::mapFeed).collect(Collectors.toList());
    }

    public static List<FeedDB> mapToFeedDBFromFeed(List<Feed> input) {
        return input.stream().map(FeedMapper::mapFeed).collect(Collectors.toList());
    }

    public static Feed mapFeed(FeedResponse feedResponse) {
        return new Feed(
                feedResponse.getUrl(),
                feedResponse.getTitle(),
                feedResponse.getDescription(),
                DateConverter.toZonedDateTime(feedResponse.getPubDate()),
                FeedState.NEW
        );
    }

    public static Feed mapFeed(FeedDB feedDB) {
        return new Feed(
                feedDB.getUrl(),
                feedDB.getTitle(),
                feedDB.getDescription(),
                DateConverter.toZonedDateTime(feedDB.getPubDate()),
                FeedState.values()[feedDB.getState()]
        );
    }

    public static FeedDB mapFeed(Feed feed) {
        return new FeedDB(
                feed.getUrl(),
                feed.getTitle(),
                feed.getDescription(),
                DateConverter.toLong(feed.getPubDate()),
                feed.getFeedState().ordinal()
        );
    }
}
