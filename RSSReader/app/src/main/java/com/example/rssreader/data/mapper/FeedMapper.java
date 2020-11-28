package com.example.rssreader.data.mapper;

import com.example.rssreader.data.Feed;
import com.example.rssreader.data.FeedDTO;
import com.example.rssreader.data.converter.DateConverter;

import java.util.List;
import java.util.stream.Collectors;

public class FeedMapper {
    public static List<Feed> map(List<FeedDTO> input) {
        return input.stream().map(
                feedDTO -> new Feed(
                        feedDTO.getUrl(),
                        feedDTO.getTitle(),
                        feedDTO.getDescription(),
                        DateConverter.toZoneDateTime(feedDTO.getPubDate())
                )
        ).collect(Collectors.toList());
    }
}
