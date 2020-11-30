package com.example.rssreader.ui.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rssreader.R;
import com.example.rssreader.data.Feed;
import com.example.rssreader.databinding.FeedListItemBinding;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedViewHolder> {

    private List<Feed> feeds = Collections.emptyList();

    private final OnItemClickListener listener;

    public FeedsAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        private final FeedListItemBinding binding;
        private Feed feed = null;

        private FeedViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.binding = FeedListItemBinding.bind(view);
            view.setOnClickListener(v -> listener.onItemClick(feed));
        }

        public void bind(Feed feed) {
            this.feed = feed;
            setTitle(feed);
            setDescription(feed);
            setPubDate(feed);
            setFeedState(feed);
        }

        private void setTitle(Feed feed) {
            binding.feedsTitle.setText(Html.fromHtml(feed.getTitle(), Html.FROM_HTML_MODE_COMPACT));
        }

        private void setDescription(Feed feed) {
            binding.feedsDescription.setText(Html.fromHtml(feed.getDescription().replaceAll("<img.+?>", ""), Html.FROM_HTML_MODE_COMPACT));
        }

        private void setPubDate(Feed feed) {
            binding.feedsPubDate.setText(feed.getPubDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        private void setFeedState(Feed feed) {
            switch (feed.getFeedState()) {
                case NEW:
                    binding.feedsState.setText(R.string.new_feed);
                    break;
                case READING:
                    binding.feedsState.setText(R.string.reading_feed);
                    break;
            }
        }

        public static FeedViewHolder create(ViewGroup viewGroup, OnItemClickListener listener) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.feed_list_item, viewGroup, false);
            return new FeedViewHolder(view, listener);
        }
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    @NotNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        return FeedViewHolder.create(viewGroup, listener);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder feedViewHolder, final int position) {
        feedViewHolder.bind(feeds.get(position));
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }
}

