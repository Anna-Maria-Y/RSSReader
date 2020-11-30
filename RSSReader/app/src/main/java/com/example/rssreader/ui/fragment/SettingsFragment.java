package com.example.rssreader.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.rssreader.R;
import com.example.rssreader.network.Result;
import com.example.rssreader.viewmodel.SettingsViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SettingsViewModel viewModel;

    private EditTextPreference editTextPreference;

    private SharedPreferences sharedPreferences;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        editTextPreference = getPreferenceScreen().findPreference(getString(R.string.user_feed_url_key));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        viewModel.getRssUrl().postValue(new Result.Success<>(sharedPreferences.getString(getString(R.string.user_feed_url_key), null)));
        observeCheckedUrl();

    }

    private void observeCheckedUrl() {
        viewModel.getRssUrl().observe(getViewLifecycleOwner(), new Observer<Result<String>>() {
            @Override
            public void onChanged(Result<String> result) {
                if (result instanceof Result.Error){
                    Toast.makeText(getContext(), result.getMessage(),Toast.LENGTH_LONG).show();
                }
                editTextPreference.setText(result.getData());
            }
        });
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.user_feed_url_key))){
            viewModel.checkUrl(sharedPreferences.getString(key, null));
        }
    }


    @Override
    public void onDestroyView () {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}