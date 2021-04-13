package com.example.submision4made.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.example.submision4made.model.Movies;

import java.util.ArrayList;

import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.CONTENT_URI;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.ID;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.MOVIE_TABLE_NAME;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.OVERVIEW;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.POSTER_PATH_STRING;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.RELEASE_DATE;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.TITLE;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.VOTE_AVERAGE;

public class MovieHelper {
    private static final String TAG = "MovieHelper";
    private static final String DATABASE_TABLE = MOVIE_TABLE_NAME;
    private static MovieDatabaseHelper movieDatabaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    public MovieHelper(Context context) {
        Log.d(TAG, "MovieHelper: running");
        movieDatabaseHelper = new MovieDatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        Log.d(TAG, "getInstance: running");
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        Log.d(TAG, "open: running");
        database = movieDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        Log.d(TAG, "close: running");
        movieDatabaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public ArrayList<Movies> getAllMovies() {
        Log.d(TAG, "getAllMovies: running");
        ArrayList<Movies> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                ID + " ASC",
                null);
        cursor.moveToFirst();
        Movies movies;
        if (cursor.getCount() > 0) {
            do {
                movies = new Movies();
                movies.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                movies.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movies.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movies.setYear(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movies.setAverage(cursor.getInt(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                movies.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH_STRING)));

                arrayList.add(movies);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertMovie(Movies movies, ContentResolver contentResolver) {

        Log.d(TAG, "insertMovie: running");
        ContentValues args = new ContentValues();
        args.put(ID, movies.getId());
        args.put(TITLE, movies.getTitle());
        args.put(OVERVIEW, movies.getOverview());
        args.put(RELEASE_DATE, movies.getYear());
        args.put(VOTE_AVERAGE, movies.getAverage());
        args.put(POSTER_PATH_STRING, movies.getPhoto());

        contentResolver.insert(CONTENT_URI, args);
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteMovie(int id, Movies movieParcel, ContentResolver contentResolver) {
        Log.d(TAG, "deleteMovie: running");
        Uri uri = Uri.parse(CONTENT_URI + "/" + movieParcel.getId());
        contentResolver.delete(uri, null, null);
        return database.delete(MOVIE_TABLE_NAME, ID + " = '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id) {
        Log.d(TAG, "queryByIdProvider: running");
        return database.query(DATABASE_TABLE,
                null
                , ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        Log.d(TAG, "queryProvider: running");
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        Log.d(TAG, "insertProvider: running");
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        Log.d(TAG, "updateProvider: running");
        return database.update(DATABASE_TABLE, values, ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        Log.d(TAG, "deleteProvider: running");
        return database.delete(DATABASE_TABLE, ID + " = ?", new String[]{id});
    }

    public Cursor query() {
        return database.query(DATABASE_TABLE, null, null, null, null, null, ID + " ASC", null);
    }

    public Cursor queryById(String id) {
        return database.query(DATABASE_TABLE, null, ID + " = ?", new String[]{id}, null, null, null, null);
    }
}
