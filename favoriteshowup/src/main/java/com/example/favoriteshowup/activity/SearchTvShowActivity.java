package com.example.favoriteshowup.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.favoriteshowup.R;
import com.example.favoriteshowup.adapter.FilteredTvShowAdapter;
import com.example.favoriteshowup.model.TvShow;
import com.example.favoriteshowup.viewModel.TvShowsViewModel;

import java.util.ArrayList;

public class SearchTvShowActivity extends AppCompatActivity {
    private static final String TAG = "SearchTvShowActivity";
    private final String STATE_QRY = "state_string";
    private String qryString;

    private TvShowsViewModel tvShowsViewModel;
    private FilteredTvShowAdapter adrFilterTvShows;
    private ArrayList<TvShow> tvShows = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: running");
        setContentView(R.layout.activity_search_tv_show);

        tvShowsViewModel = new ViewModelProvider(this).get(TvShowsViewModel.class);
        tvShowsViewModel.getFilteredTvShows().observe(this, getTvShows);

        RecyclerView rvFilteredTvShow = findViewById(R.id.rv_fragment_search_tv_show);
        rvFilteredTvShow.setHasFixedSize(true);
        rvFilteredTvShow.setLayoutManager(new LinearLayoutManager(this));

        adrFilterTvShows = new FilteredTvShowAdapter(this);
        adrFilterTvShows.setTvShows(tvShows);

        rvFilteredTvShow.setAdapter(adrFilterTvShows);

        this.setTitle(R.string.search_tv_show);

        if (savedInstanceState != null) {
            qryString = savedInstanceState.getString(STATE_QRY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: running");
        outState.putString(STATE_QRY, searchView.getQuery().toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: running");
        getMenuInflater().inflate(R.menu.menu_search_tv_show, menu);

        MenuItem searchTvShowMenuItem = menu.findItem(R.id.action_search_tv_show);
        searchView = (SearchView) searchTvShowMenuItem.getActionView();

        searchView.onActionViewExpanded();

        if (qryString != null){
            searchView.setQuery(qryString, false);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                tvShowsViewModel.setTvShows(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private Observer<ArrayList<TvShow>> getTvShows = new Observer<ArrayList<TvShow>>() {
        @Override
        public void onChanged(@Nullable ArrayList<TvShow> tvShowsItem) {
            if (tvShowsItem != null) {
                tvShows = tvShowsItem;
                adrFilterTvShows.setData(tvShowsItem);
            }
        }
    };
}
