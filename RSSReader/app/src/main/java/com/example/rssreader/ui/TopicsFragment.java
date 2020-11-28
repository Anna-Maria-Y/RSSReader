package com.example.rssreader.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rssreader.R;
import com.example.rssreader.data.Feed;
import com.example.rssreader.databinding.TopicsFragmentBinding;
import com.example.rssreader.ui.adapter.FeedsAdapter;
import com.example.rssreader.viewmodel.TopicsViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TopicsFragment extends Fragment {

    private TopicsViewModel topicsViewModel;

    private TopicsFragmentBinding binding;

    public static TopicsFragment newInstance() {
        return new TopicsFragment();
    }

    private final FeedsAdapter adapter = new FeedsAdapter();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TopicsFragmentBinding.inflate(inflater);
        initAdapter();
        return binding.getRoot();
    }

    private void initAdapter() {
        binding.boardgamesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        topicsViewModel = new ViewModelProvider(this).get(TopicsViewModel.class);
        topicsViewModel.loadFeeds();
        observeFeeds();
    }

    private void observeFeeds(){
        final Observer<List<Feed>> feedsObserver = new Observer<List<Feed>>() {
            @Override
            public void onChanged(@Nullable final List<Feed> feeds) {
                adapter.setFeeds(feeds);
                adapter.notifyDataSetChanged();
            }
        };

        topicsViewModel.getFeeds().observe(getViewLifecycleOwner(), feedsObserver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}