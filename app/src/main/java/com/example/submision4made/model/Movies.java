package com.example.submision4made.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.submision4made.Config;

import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.OVERVIEW;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.POSTER_PATH_STRING;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.RELEASE_DATE;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.TITLE;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.VOTE_AVERAGE;
import static com.example.submision4made.db.MovieDatabaseContract.getColumnInt;
import static com.example.submision4made.db.MovieDatabaseContract.getColumnString;

public class Movies implements Parcelable {
    private static final String TAG = "Movies";

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    private int id;
    private String title; // title
    private String year; // release_date
    private int average; //vote_average
    private String overview; // overview
    private String photo; // poster path


    protected Movies(Parcel in) {
        Log.d(TAG, "Movies: running");
        id = in.readInt();
        title = in.readString();
        year = in.readString();
        average = in.readInt();
        overview = in.readString();
        photo = in.readString();
    }

    public Movies() {
    }

    public Movies(int id, String title, String date, String photo, int rating, String overview) {
        this.id = id;
        this.title = title;
        this.year = date;
        this.photo = photo;
        this.average = rating;
        this.overview = overview;
    }

    public Movies(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, TITLE);
        this.year = getColumnString(cursor, RELEASE_DATE);
        this.photo = getColumnString(cursor, POSTER_PATH_STRING);;
        this.average = getColumnInt(cursor, VOTE_AVERAGE);
        this.overview = getColumnString(cursor, OVERVIEW);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(year);
        parcel.writeInt(average);
        parcel.writeString(overview);
        parcel.writeString(photo);
    }

    public Movies(JSONObject object){
        try {
            int id = object.getInt("id");
            String title = object.getString("title");
            String year = object.getString("release_date");
            int average = object.getInt("vote_average");
            String overview = object.getString("overview");
            String photo = Config.URL_IMAGE_BASE + object.getString("poster_path");

            this.id = id;
            this.title = title;
            this.year = year;
            this.average = average;
            this.overview = overview;
            this.photo = photo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
