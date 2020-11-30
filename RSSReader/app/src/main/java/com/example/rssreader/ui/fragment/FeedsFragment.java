package com.example.rssreader.ui.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rssreader.R;
import com.example.rssreader.data.Feed;
import com.example.rssreader.databinding.FeedsFragmentBinding;
import com.example.rssreader.network.Result;
import com.example.rssreader.ui.adapter.FeedsAdapter;
import com.example.rssreader.ui.adapter.OnItemClickListener;
import com.example.rssreader.viewmodel.FeedsViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FeedsFragment extends Fragment {

    private FeedsViewModel feedsViewModel;

    private FeedsFragmentBinding binding;

    private SharedPreferences sharedPreferences;

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FeedsFragmentBinding.inflate(inflater);
        initAdapter();
        initSwipeRefresh();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        feedsViewModel = new ViewModelProvider(this).get(FeedsViewModel.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        loadFeeds();
        observeLoading();
        observeErrorMessage();
        observeFeeds();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_settings) {
            toSettingsFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void observeFeeds() {
        final Observer<List<Feed>> feedsObserver = feeds -> {
            if (feeds != null) {
                    FeedsAdapter adapter = (FeedsAdapter) binding.recyclerView.getAdapter();
                    adapter.setFeeds(feeds);
                    adapter.notifyDataSetChanged();
                }
            };
        feedsViewModel.getFeeds().observe(getViewLifecycleOwner(), feedsObserver);
    }

    private void observeLoading() {
        final Observer<Boolean> loadingObserver = loading -> {
            if (loading != null) {
                binding.swipeRefresh.setRefreshing(loading);
            }
        };
        feedsViewModel.getLoading().observe(getViewLifecycleOwner(), loadingObserver);
    }

    private void observeErrorMessage() {
        final Observer<String> errorObserver = errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        };
        feedsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorObserver);
    }

    private void initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(this::loadFeeds);
    }

    private void initAdapter() {
        FeedsAdapter adapter = new FeedsAdapter((OnItemClickListener<Feed>) feed -> {
            if (feed != null) {
                toWebViewFragment(feed.getUrl());
            }
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadFeeds(){
        String url = sharedPreferences.getString(getString(R.string.user_feed_url_key), null);
        feedsViewModel.loadFeeds(url);
    }

    private void toWebViewFragment(String url){
        NavDirections action = FeedsFragmentDirections.actionWebView(url);
        NavHostFragment.findNavController(this).navigate(action);
    }

    private void toSettingsFragment(){
        NavDirections action = FeedsFragmentDirections.actionSettingsFragment();
        NavHostFragment.findNavController(this).navigate(action);
    }
}