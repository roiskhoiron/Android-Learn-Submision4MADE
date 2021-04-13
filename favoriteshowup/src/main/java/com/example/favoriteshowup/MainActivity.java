package com.example.favoriteshowup;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.favoriteshowup.adapter.SectionPagerAdapter;
import com.example.favoriteshowup.fragment.MoviesFragment;
import com.example.favoriteshowup.fragment.TvShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public String title = "page";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MoviesFragment movieFragment;
    private TvShowFragment tvShowFragment;
    private BottomNavigationView navigationView;

    private final String STATE_TITLE = "state_string";
    private final String STATE_MODE = "state_mode";
    private int mode = R.id.navigation_movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: running");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: running");
        setContentView(R.layout.activity_favorite);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_favorite);

        callAdapter();

        /*title = getResources().getString(R.string.title_movie);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        movieFragment = new MoviesFragment();
        tvShowFragment = new TvShowFragment();

        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt(STATE_MODE);
            title = savedInstanceState.getString(STATE_TITLE);

            if (mode == R.id.navigation_movie) {
                showMoviesFragment();
            } else {
                showTvShowFragment();
            }
        } else {
            showMoviesFragment();
        }*/
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

    private void callAdapter() {
        Log.d(TAG, "callAdapter: running");
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), this);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        getSupportActionBar().setElevation(0);
    }

/*    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_movie:
                    title = getResources().getString(R.string.title_movie);
                    mode = menuItem.getItemId();

                    showMoviesFragment();
                    Log.d(TAG, "onNavigationItemSelected() returned: " + title);
                    return true;

                case R.id.navigation_tv_show:
                    title = getResources().getString(R.string.title_tv_show);
                    mode = menuItem.getItemId();

                    showTvShowFragment();
                    Log.d(TAG, "onNavigationItemSelected() returned: " + title);
                    return true;

                case R.id.navigation_favorite:
                    title = getResources().getString(R.string.title_favorite);
                    mode = menuItem.getItemId();

                    Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                    startActivity(intent);

                    Log.d(TAG, "onNavigationItemSelected() returned: " + title);
                    return true;
            }
            return false;
        }
    };

    private void showMoviesFragment() {
        Log.d(TAG, "showMoviesFragment: running");
        setTitle(title);

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(MoviesFragment.class.getSimpleName());
        if (!(fragment instanceof MoviesFragment)) {
            fragmentTransaction.replace(
                    R.id.view_pager,
                    movieFragment,
                    MoviesFragment.class.getSimpleName()
            );
            fragmentTransaction.commit();
        }
    }

    private void showTvShowFragment() {
        Log.d(TAG, "showTvShowFragment: running");
        setTitle(title);

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(TvShowFragment.class.getSimpleName());
        if (!(fragment instanceof TvShowFragment)) {
            fragmentTransaction.replace(
                    R.id.view_pager,
                    tvShowFragment,
                    TvShowFragment.class.getSimpleName()
            );
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        Log.d(TAG, "onCreatePanelMenu: running");
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: runnning");
        switch (item.getItemId()) {
            case R.id.setting:
                Intent changeLanguageSettingIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(changeLanguageSettingIntent);
                break;

            case R.id.action_search_movie:
                Intent intentSearchMovie = new Intent(this, SearchMovieActivity.class);
                startActivity(intentSearchMovie);
                break;

            case R.id.action_search_tv_show:
                Intent intentSearchTvShow = new Intent(this, SearchTvShowActivity.class);
                startActivity(intentSearchTvShow);
                break;
            case R.id.action_reminder_setting:
                Intent intentReminderSetting = new Intent(this, SettingNotificationActivity.class);
                startActivity(intentReminderSetting);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
