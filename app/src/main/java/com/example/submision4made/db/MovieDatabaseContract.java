package com.example.submision4made.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieDatabaseContract {
    public static final String AUTHORITY = "com.example.submision4made";
    public static final String SCHEME = "content";

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static final class MovieColumns implements BaseColumns {
        public static final String MOVIE_TABLE_NAME = "movie";

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE= "release_date";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String POSTER_PATH_STRING = "poster_path_string";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(MOVIE_TABLE_NAME)
                .build();
    }
}
