package com.example.submision4made;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;

import com.example.submision4made.db.MovieDatabaseContract;
import com.example.submision4made.db.MovieHelper;
import com.example.submision4made.db.TvShowDatabaseContract;
import com.example.submision4made.db.TvShowHelper;
import com.example.submision4made.fragment.FavoriteMovieFragment;
import com.example.submision4made.fragment.FavoriteTvShowFragment;

import static com.example.submision4made.db.MovieDatabaseContract.AUTHORITY;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.CONTENT_URI;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.MOVIE_TABLE_NAME;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.TV_SHOW_TABLE_NAME;

public class FavoriteProvider extends ContentProvider {
    public static final int MOVIE = 1;
    public static final int MOVIE_ID = 11;
    public static final int TVSHOW = 2;
    public static final int TVSHOW_ID = 22;

    public static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private MovieHelper movieHelper;
    private TvShowHelper tvShowHelper;

    static {
        URI_MATCHER.addURI(AUTHORITY, MOVIE_TABLE_NAME, MOVIE);
        URI_MATCHER.addURI(AUTHORITY,
                MOVIE_TABLE_NAME + "/#",
                MOVIE_ID);
        URI_MATCHER.addURI(AUTHORITY, TV_SHOW_TABLE_NAME, TVSHOW);
        URI_MATCHER.addURI(AUTHORITY,
                TV_SHOW_TABLE_NAME + "/#",
                TVSHOW_ID);
    }

    public FavoriteProvider() {
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        movieHelper = MovieHelper.getInstance(getContext());
        tvShowHelper = TvShowHelper.getInstance(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor;
        tvShowHelper.open();
        movieHelper.open();
        switch (URI_MATCHER.match(uri)) {
            case MOVIE:
                cursor = movieHelper.query();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryById(uri.getLastPathSegment());
                break;
            case TVSHOW:
                cursor = tvShowHelper.query();
                break;
            case TVSHOW_ID:
                cursor = tvShowHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        Uri uri_;
        long added;
        tvShowHelper.open();
        movieHelper.open();
        switch (URI_MATCHER.match(uri)) {
            case MOVIE:
                added = movieHelper.insertProvider(values);
                uri_ = Uri.parse(CONTENT_URI + "/" + added);
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteMovieFragment.DataObserver(new Handler(), getContext()));
                }
                break;
            case TVSHOW:
                added = tvShowHelper.insertProvider(values);
                uri_ = Uri.parse(TvShowDatabaseContract.TvShowColumns.CONTENT_URI + "/" + added);
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(TvShowDatabaseContract.TvShowColumns.CONTENT_URI, new FavoriteTvShowFragment.DataObserver(new Handler(), getContext()));
                }
                break;
            default:
                throw new SQLException("FailedAdded " + uri);
        }

        return uri_;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        tvShowHelper.open();
        movieHelper.open();
        int drop;
        switch (URI_MATCHER.match(uri)) {
            case MOVIE_ID:
                drop = movieHelper.deleteProvider(uri.getLastPathSegment());
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(MovieDatabaseContract.MovieColumns.CONTENT_URI, new FavoriteMovieFragment.DataObserver(new Handler(), getContext()));
                }
                break;
            case TVSHOW_ID:
                drop = tvShowHelper.deleteProvider(uri.getLastPathSegment());
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(TvShowDatabaseContract.TvShowColumns.CONTENT_URI, new FavoriteTvShowFragment.DataObserver(new Handler(), getContext()));
                }
                break;
            default:
                drop = 0;
                break;
        }
        return drop;
    }
}
