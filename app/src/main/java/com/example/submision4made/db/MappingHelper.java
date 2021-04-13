package com.example.submision4made.db;

import android.database.Cursor;

import com.example.submision4made.model.Movies;
import com.example.submision4made.model.TvShow;

import java.util.ArrayList;

import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.ID;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.RELEASE_DATE;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.TITLE;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.FIRST_AIR_DATE;
import static com.example.submision4made.db.TvShowDatabaseContract.TvShowColumns.NAME;


public class MappingHelper {
    private static final String TAG = "MappingHelper";
    public static ArrayList<Movies> mapCursorToArrayList(Cursor itemCursor) {
        ArrayList<Movies> itemList = new ArrayList<>();

        while (itemCursor.moveToNext()) {
            int id = itemCursor.getInt(itemCursor.getColumnIndexOrThrow(ID));
            String title = itemCursor.getString(itemCursor.getColumnIndexOrThrow(TITLE));
            String date = itemCursor.getString(itemCursor.getColumnIndexOrThrow(RELEASE_DATE));
            String photo = itemCursor.getString(itemCursor.getColumnIndexOrThrow(MovieDatabaseContract.MovieColumns.POSTER_PATH_STRING));
            int rating = itemCursor.getInt(itemCursor.getColumnIndexOrThrow(MovieDatabaseContract.MovieColumns.VOTE_AVERAGE));
            String overview = itemCursor.getString(itemCursor.getColumnIndexOrThrow(MovieDatabaseContract.MovieColumns.OVERVIEW));

            itemList.add(new Movies(id, title, date, photo, rating, overview));
        }
        return itemList;
    }

    public static ArrayList<TvShow> mapCursorTvToArrayList(Cursor itemCursor) {
        ArrayList<TvShow> itemList = new ArrayList<>();

        while (itemCursor.moveToNext()) {
            int id = itemCursor.getInt(itemCursor.getColumnIndexOrThrow(TvShowDatabaseContract.TvShowColumns.ID));
            String title = itemCursor.getString(itemCursor.getColumnIndexOrThrow(NAME));
            String date = itemCursor.getString(itemCursor.getColumnIndexOrThrow(FIRST_AIR_DATE));
            String photo = itemCursor.getString(itemCursor.getColumnIndexOrThrow(TvShowDatabaseContract.TvShowColumns.POSTER_PATH_STRING));
            int rating = itemCursor.getInt(itemCursor.getColumnIndexOrThrow(TvShowDatabaseContract.TvShowColumns.VOTE_AVERAGE));
            String overview = itemCursor.getString(itemCursor.getColumnIndexOrThrow(TvShowDatabaseContract.TvShowColumns.OVERVIEW));

            itemList.add(new TvShow(id, title, photo, overview));
        }
        return itemList;
    }
}
