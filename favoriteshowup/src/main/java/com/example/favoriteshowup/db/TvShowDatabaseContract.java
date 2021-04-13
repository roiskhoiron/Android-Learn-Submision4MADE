package com.example.favoriteshowup.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class TvShowDatabaseContract {
    public static final String AUTHORITY = "com.example.submision4made";
    public static final String SCHEME = "content";

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static final class TvShowColumns implements BaseColumns {
        public static final String TV_SHOW_TABLE_NAME = "tvshow";

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String OVERVIEW = "overview";
        public static final String FIRST_AIR_DATE = "first_air_date";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String POSTER_PATH_STRING = "poster_path_string";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TV_SHOW_TABLE_NAME)
                .build();
    }
}
