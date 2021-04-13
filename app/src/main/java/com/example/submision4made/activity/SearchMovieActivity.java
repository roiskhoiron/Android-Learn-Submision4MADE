package com.example.submision4made.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.submision4made.R;
import com.example.submision4made.adapter.FilteredMoviesAdapter;
import com.example.submision4made.model.Movies;
import com.example.submision4made.viewModel.MoviesViewModel;

import java.util.ArrayList;

public class SearchMovieActivity extends AppCompatActivity {
    private static final String TAG = "SearchMovieActivity";
    private final String STATE_QRY = "state_string";
    private String qryString;

    private MoviesViewModel moviesViewModel;
    private FilteredMoviesAdapter adrFilterMovies;
    private ArrayList<Movies> movies = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: running");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        moviesViewModel.getFilteredMovies().observe(this, getMovies);

        RecyclerView rvFilteredMovies = findViewById(R.id.rv_fragment_search_movie);
        rvFilteredMovies.setHasFixedSize(true);
        rvFilteredMovies.setLayoutManager(new LinearLayoutManager(this));

        adrFilterMovies = new FilteredMoviesAdapter(this);
        adrFilterMovies.setMovies(movies);

        rvFilteredMovies.setAdapter(adrFilterMovies);

        this.setTitle(R.string.search_movie);

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
        getMenuInflater().inflate(R.menu.menu_search_movie, menu);

        MenuItem searchMovieMenuItem = menu.findItem(R.id.action_search_movie);
        searchView = (SearchView) searchMovieMenuItem.getActionView();

        searchView.onActionViewExpanded();

        if (qryString != null) {
            searchView.setQuery(qryString, false);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "onQueryTextSubmit: running");
                moviesViewModel.setMovies(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "onQueryTextChange: running");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private Observer<ArrayList<Movies>> getMovies = new Observer<ArrayList<Movies>>() {
        @Override
        public void onChanged(ArrayList<Movies> movieItems) {
            Log.d(TAG, "onChanged: running");
            if (movieItems != null) {
                movies = movieItems;
                adrFilterMovies.setData(movieItems);
            }
        }
    };
}
