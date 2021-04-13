package com.example.submision4made.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.submision4made.db.MovieDatabaseContract.MovieColumns;

import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.MOVIE_TABLE_NAME;

public class MovieDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "MovieDatabaseHelper";

    private static final String MOVIE_DATABASE_NAME = "dbmovie";

    private static final int MOVIE_DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s" +
                    " (%s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL)",
            MOVIE_TABLE_NAME,
            MovieColumns.ID,
            MovieColumns.TITLE,
            MovieColumns.OVERVIEW,
            MovieColumns.RELEASE_DATE,
            MovieColumns.VOTE_AVERAGE,
            MovieColumns.POSTER_PATH_STRING
    );

    MovieDatabaseHelper(Context context) {
        super(context, MOVIE_DATABASE_NAME, null, MOVIE_DATABASE_VERSION);
        Log.d(TAG, "MovieDatabaseHelper: Running");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: Running");
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade: running");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}