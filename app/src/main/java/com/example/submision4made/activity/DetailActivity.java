package com.example.submision4made.activity;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.submision4made.R;
import com.example.submision4made.db.MovieHelper;
import com.example.submision4made.db.TvShowDatabaseContract;
import com.example.submision4made.db.TvShowHelper;
import com.example.submision4made.model.Movies;
import com.example.submision4made.model.TvShow;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.CONTENT_URI;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.ID;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.OVERVIEW;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.POSTER_PATH_STRING;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.RELEASE_DATE;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.TITLE;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.VOTE_AVERAGE;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.FIRST_AIR_DATE;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.NAME;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    private TvShow tvShowParcel;
    private Movies movieParcel;

    public static final String MOVIE_INDEX = "parcel movie";
    public static final String TVSHOW_INDEX = "parcel tvShow";
    private boolean isFavorite;
    private int typeData;

    private ProgressBar progressBar;
    private Menu menu;

    private MovieHelper movieHelper;
    private TvShowHelper tvShowHelper;

    private TextView tvTitle;
    private TextView tvRelease;
    private TextView tvDirector;
    private TextView tvOverview;
    private ImageView imgvPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: running");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvTitle = findViewById(R.id.tv_title);
        tvRelease = findViewById(R.id.tv_release);
        tvDirector = findViewById(R.id.tv_director);
        tvOverview = findViewById(R.id.tv_overview);
        imgvPhoto = findViewById(R.id.imgv_photo);
        progressBar = findViewById(R.id.progressBar);


        movieParcel = getIntent().getParcelableExtra(MOVIE_INDEX);
        tvShowParcel = getIntent().getParcelableExtra(TVSHOW_INDEX);

        if (movieParcel != null) {
            Log.d(TAG, "onCreate: movies detected");
            movieHelper = MovieHelper.getInstance(getApplicationContext());
            movieHelper.open();

            showMovieParcel(movieParcel);
        } else if (tvShowParcel != null) {
            Log.d(TAG, "onCreate: tvshows detected");
            tvShowHelper = TvShowHelper.getInstance(getApplicationContext());
            tvShowHelper.open();

            showTvShowParcel(tvShowParcel);
        } else {
            Log.e(TAG, "onCreate: Error");
            showLoading(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showTvShowParcel(TvShow tvShowParcel) {
        Log.d(TAG, "showTvShowParcel: running");
        this.typeData = 2;
        String vote_average = Integer.toString(tvShowParcel.getAverage());
        String url_image = tvShowParcel.getPhoto();

        Objects.requireNonNull(getSupportActionBar()).setTitle("TvShow");
        tvTitle.setText(tvShowParcel.getTitle());
        tvRelease.setText(tvShowParcel.getYear());
        tvDirector.setText(vote_average);
        tvOverview.setText(tvShowParcel.getOverview());
        Glide.with(this)
                .load(url_image)
                .apply(new RequestOptions().override(100, 140))
                .into(imgvPhoto);
        showLoading(false);

        isFavorite = false;
        checkFavorite();
    }

    private void showMovieParcel(Movies movieParcel) {
        Log.d(TAG, "showMovieParcel: running");
        this.typeData = 1;
        String vote_average = Integer.toString(movieParcel.getAverage());
        String url_image = movieParcel.getPhoto();

        Objects.requireNonNull(getSupportActionBar()).setTitle("Movie");
        tvTitle.setText(movieParcel.getTitle());
        tvRelease.setText(movieParcel.getYear());
        tvDirector.setText(vote_average);
        tvOverview.setText(movieParcel.getOverview());
        Glide.with(this)
                .load(url_image)
                .apply(new RequestOptions().override(100, 140))
                .into(imgvPhoto);
        showLoading(false);

        isFavorite = false;
        checkFavorite();
    }

    private void checkFavorite() {
        if (typeData == 1) {
            Log.d(TAG, "checkFavorite: Movie");
            ArrayList<Movies> moviesInDatabase = movieHelper.getAllMovies();

            for (Movies movie : moviesInDatabase) {

                if (this.movieParcel.getId() == movie.getId()) {
                    isFavorite = true;
                    Log.d(TAG, "checkFavorite: Movie Favorite detected");
                }

                if (isFavorite == true) {
                    break;
                }
            }
        } else if (typeData == 2) {
            Log.d(TAG, "checkFavorite: TvShow");
            ArrayList<TvShow> tvShowsInDatabase = tvShowHelper.getAllTvShows();

            for (TvShow tvShow : tvShowsInDatabase) {

                if (this.tvShowParcel.getId() == tvShow.getId()) {
                    isFavorite = true;
                    Log.d(TAG, "checkFavorite: TvShow Favorite detected");
                }

                if (isFavorite == true) {
                    break;
                }
            }
        }
    }

    private void showLoading(Boolean state) {
        progressBar = findViewById(R.id.progressBar);
        if (state) {
            Log.d(TAG, "showLoading: on");
            progressBar.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "showLoading: off");
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        setIconFavorite();

        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_add_favorite_menu_detail) {
            favoriteButtonPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setIconFavorite() {
        if (isFavorite) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_added_favorites));
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_add_favorites));
        }
    }

    private void favoriteButtonPressed() {
        if (typeData == 1) {
            Log.d(TAG, "favoriteButtonPressed: Movies");
            if (isFavorite) {
                Log.d(TAG, "favoriteButtonPressed: Movies detected and deleted");
                //movieHelper.deleteMovie(movieParcel.getId(), movieParcel, getContentResolver());
                Uri uriMv = Uri.parse(CONTENT_URI + "/" + movieParcel.getId());
                getContentResolver().delete(uriMv, null, null);

            } else {
                Log.d(TAG, "favoriteButtonPressed: Movies undetected and inserted");
                // movieHelper.insertMovie(movieParcel, getContentResolver());
                ContentValues args = new ContentValues();
                args.put(ID, movieParcel.getId());
                args.put(TITLE, movieParcel.getTitle());
                args.put(OVERVIEW, movieParcel.getOverview());
                args.put(RELEASE_DATE, movieParcel.getYear());
                args.put(VOTE_AVERAGE, movieParcel.getAverage());
                args.put(POSTER_PATH_STRING, movieParcel.getPhoto());

                getContentResolver().insert(CONTENT_URI, args);
            }
        } else if (typeData == 2) {
            Log.d(TAG, "favoriteButtonPressed: TvShow");
            if (isFavorite) {
                Log.d(TAG, "favoriteButtonPressed: TvShow detected and deleted");
                //tvShowHelper.deleteTvShow(tvShowParcel.getId(), tvShowParcel, getContentResolver());
                Uri uriTv = Uri.parse(TvShowDatabaseContract.TvShowColumns.CONTENT_URI + "/" + tvShowParcel.getId());
                getContentResolver().delete(uriTv, null, null);

            } else {
                Log.d(TAG, "favoriteButtonPressed: TvShow detected and inserted");
                //tvShowHelper.insertTvShow(tvShowParcel, getContentResolver());
                ContentValues args = new ContentValues();
                args.put(TvShowDatabaseContract.TvShowColumns.ID, tvShowParcel.getId());
                args.put(NAME, tvShowParcel.getTitle());
                args.put(TvShowDatabaseContract.TvShowColumns.OVERVIEW, tvShowParcel.getOverview());
                args.put(FIRST_AIR_DATE, tvShowParcel.getYear());
                args.put(TvShowDatabaseContract.TvShowColumns.VOTE_AVERAGE, tvShowParcel.getAverage());
                args.put(TvShowDatabaseContract.TvShowColumns.POSTER_PATH_STRING, tvShowParcel.getPhoto());

                getContentResolver().insert(TvShowDatabaseContract.TvShowColumns.CONTENT_URI, args);
            }
        }
        isFavorite = !isFavorite;
        setIconFavorite();
    }
}
