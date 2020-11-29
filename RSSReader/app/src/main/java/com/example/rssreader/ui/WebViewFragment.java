package com.example.rssreader.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.rssreader.R;
import com.example.rssreader.databinding.WebViewFragmentBinding;
import com.example.rssreader.viewmodel.WebViewViewModel;

public class WebViewFragment extends Fragment {

    private WebViewViewModel viewModel;

    private WebViewFragmentBinding binding;

    private WebView webView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = WebViewFragmentBinding.inflate(inflater);
        initWebView(savedInstanceState);
        initSwipeRefresh();
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        webView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.bottomAppBar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(WebViewViewModel.class);
        // TODO: Use ViewModel. Reading/done
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.web_view_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_back: {
                back();
                return true;
            }
            case R.id.app_bar_forward: {
                forward();
                return true;
            }
            case R.id.app_bar_refresh: {
                refresh();
                return true;
            }
            case R.id.app_bar_stop: {
                stop();
                return true;
            }
            case R.id.app_bar_browser: {
                loadOnBrowser();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initWebView(@Nullable Bundle savedInstanceState) {
        webView = binding.webView;
        if (savedInstanceState != null)
            webView.restoreState(savedInstanceState);
        else {
            webView.setWebViewClient(getWebViewClient());
            webView.loadUrl(urlFromBundle());
        }
    }

    public WebViewClient getWebViewClient(){
        return new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.swipeRefresh.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.swipeRefresh.setRefreshing(false);
            }
        };
    }

    public void initSwipeRefresh(){
        binding.swipeRefresh.setOnRefreshListener(this::refresh);
    }

    private String urlFromBundle() {
        return WebViewFragmentArgs.fromBundle(getArguments()).getPageUrl();
    }

    private void back() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    private void forward() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }

    private void refresh() {
        webView.reload();
    }

    private void stop() {
        getActivity().onBackPressed();
    }

    private void loadOnBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(webView.getUrl()));
        startActivity(browserIntent);
    }
}