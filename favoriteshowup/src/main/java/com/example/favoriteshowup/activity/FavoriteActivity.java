package com.example.favoriteshowup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.favoriteshowup.R;
import com.example.favoriteshowup.adapter.SectionPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class FavoriteActivity extends AppCompatActivity {
    private static final String TAG = "FavoriteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: running");
        setContentView(R.layout.activity_favorite);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_favorite);

        callAdapter();

    }

    private void callAdapter() {
        Log.d(TAG, "callAdapter: running");
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), this);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        getSupportActionBar().setElevation(0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "onSupportNavigateUp: running");
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        Log.d(TAG, "onCreatePanelMenu: running");
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: running");
        if (item.getItemId() == R.id.setting) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: running");
    }

    @Override
    protected void onResume() {
        super.onResume();
        callAdapter();
        Log.d(TAG, "onResume: running");
    }
}
