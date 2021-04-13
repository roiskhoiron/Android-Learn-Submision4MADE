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

import com.example.submision4made.model.TvShow;

import java.util.ArrayList;

import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.CONTENT_URI;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.FIRST_AIR_DATE;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.ID;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.NAME;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.OVERVIEW;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.POSTER_PATH_STRING;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.TV_SHOW_TABLE_NAME;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.VOTE_AVERAGE;

public class TvShowHelper {
    private static final String TAG = "TvShowHelper";
    private static final String DATABASE_TABLE = TV_SHOW_TABLE_NAME;
    private static TvShowDatabaseHelper tvShowDatabaseHelper;
    private static TvShowHelper INSTANCE;

    private static SQLiteDatabase database;

    private TvShowHelper(Context context) {
        Log.d(TAG, "TvShowHelper: running");
        tvShowDatabaseHelper = new TvShowDatabaseHelper(context);
    }

    public static TvShowHelper getInstance(Context context) {
        Log.d(TAG, "getInstance: running");
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                Log.d(TAG, "getInstance sync : running ");
                if (INSTANCE == null) {
                    INSTANCE = new TvShowHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        Log.d(TAG, "open: running");
        database = tvShowDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        Log.d(TAG, "close: running");
        tvShowDatabaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public ArrayList<TvShow> getAllTvShows() {
        Log.d(TAG, "getAllTvShows: runnning");
        ArrayList<TvShow> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                ID + " ASC",
                null);
        cursor.moveToFirst();
        TvShow tvShow;
        if (cursor.getCount() > 0) {
            do {
                tvShow = new TvShow();
                tvShow.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                tvShow.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
                tvShow.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                tvShow.setYear(cursor.getString(cursor.getColumnIndexOrThrow(FIRST_AIR_DATE)));
                tvShow.setAverage(cursor.getInt(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                tvShow.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH_STRING)));

                arrayList.add(tvShow);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertTvShow(TvShow tvShow, ContentResolver contentResolver) {
        Log.d(TAG, "insertTvShow: running");
        ContentValues args = new ContentValues();
        args.put(ID, tvShow.getId());
        args.put(NAME, tvShow.getTitle());
        args.put(OVERVIEW, tvShow.getOverview());
        args.put(FIRST_AIR_DATE, tvShow.getYear());
        args.put(VOTE_AVERAGE, tvShow.getAverage());
        args.put(POSTER_PATH_STRING, tvShow.getPhoto());

        contentResolver.insert(CONTENT_URI, args);
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteTvShow(int id, TvShow tvShowParcel, ContentResolver contentResolver) {
        Log.d(TAG, "deleteTvShow: running");
        Uri uri = Uri.parse(CONTENT_URI + "/" + tvShowParcel.getId());
        contentResolver.delete(uri, null, null);
        return database.delete(TV_SHOW_TABLE_NAME, ID + " = '" + id + "'", null);
    }

    public Cursor query() {
        Log.d(TAG, "query: running");
        return database.query(DATABASE_TABLE, null, null, null, null, null, ID + " ASC", null);
    }

    public Cursor queryById(String id) {
        Log.d(TAG, "queryById: running");
        return database.query(DATABASE_TABLE, null, ID + " = ?", new String[]{id}, null, null, null, null);
    }

    public long insertProvider(ContentValues values) {
        Log.d(TAG, "insertProvider: running");
        return database.insert(DATABASE_TABLE, null, values);
    }
    public int deleteProvider(String id) {
        Log.d(TAG, "deleteProvider: running");
        return database.delete(DATABASE_TABLE, ID + " = ?", new String[]{id});
    }
}