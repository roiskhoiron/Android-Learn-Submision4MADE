package com.example.submision4made.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns;

import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.TV_SHOW_TABLE_NAME;

public class TvShowDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "TvShowDatabaseHelper";

    private static final String TV_SHOW_DATABASE_NAME = "dbtvshow";

    private static final int TV_SHOW_DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_TV_SHOW = String.format("CREATE TABLE %s" +
                    " (%s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL)",
            TV_SHOW_TABLE_NAME,
            TvShowColumns.ID,
            TvShowColumns.NAME,
            TvShowColumns.OVERVIEW,
            TvShowColumns.FIRST_AIR_DATE,
            TvShowColumns.VOTE_AVERAGE,
            TvShowColumns.POSTER_PATH_STRING
    );

    TvShowDatabaseHelper(Context context) {
        super(context, TV_SHOW_DATABASE_NAME, null, TV_SHOW_DATABASE_VERSION);
        Log.d(TAG, "TvShowDatabaseHelper: running");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: running");
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_TV_SHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade: running");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TV_SHOW_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}