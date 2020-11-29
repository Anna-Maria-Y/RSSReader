package com.example.rssreader.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rssreader.data.Feed;
import com.example.rssreader.databinding.TopicsFragmentBinding;
import com.example.rssreader.network.Result;
import com.example.rssreader.ui.adapter.FeedsAdapter;
import com.example.rssreader.ui.adapter.OnItemClickListener;
import com.example.rssreader.viewmodel.TopicsViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FeedsFragment extends Fragment {

    private TopicsViewModel topicsViewModel;

    private TopicsFragmentBinding binding;

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TopicsFragmentBinding.inflate(inflater);
        initAdapter();
        initSwipeRefresh();
        return binding.getRoot();
    }

    private void initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(() -> topicsViewModel.loadFeeds());
    }

    private void initAdapter() {
        FeedsAdapter adapter = new FeedsAdapter((OnItemClickListener<Feed>) feed -> {
            if (feed!=null){
                NavDirections action = FeedsFragmentDirections.actionWebView(feed.getUrl());
                NavHostFragment.findNavController(this).navigate(action);
            }
        });
        binding.boardgamesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        topicsViewModel = new ViewModelProvider(this).get(TopicsViewModel.class);
        topicsViewModel.loadFeeds();
        observeLoading();
        observeFeedsResult();
    }

    private void observeFeedsResult() {
        final Observer<Result<List<Feed>>> feedsObserver = result -> {
            if (result != null) {
                if (result instanceof Result.Error) {
                    Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                }
                else if (result instanceof Result.Success){
                    FeedsAdapter adapter = (FeedsAdapter) binding.boardgamesRecyclerView.getAdapter();
                    adapter.setFeeds(result.getData());
                    adapter.notifyDataSetChanged();
                }
            }
        };
        topicsViewModel.getFeeds().observe(getViewLifecycleOwner(), feedsObserver);
    }

    private void observeLoading() {
        final Observer<Boolean> loadingObserver = loading -> {
            if (loading != null) {
                binding.swipeRefresh.setRefreshing(loading);
            }
        };
        topicsViewModel.getLoading().observe(getViewLifecycleOwner(), loadingObserver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}