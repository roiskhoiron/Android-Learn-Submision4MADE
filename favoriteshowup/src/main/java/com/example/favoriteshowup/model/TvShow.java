package com.example.favoriteshowup.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.favoriteshowup.Config;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.favoriteshowup.db.TvShowDatabaseContract.TvShowColumns.ID;
import static com.example.favoriteshowup.db.TvShowDatabaseContract.TvShowColumns.NAME;
import static com.example.favoriteshowup.db.TvShowDatabaseContract.TvShowColumns.OVERVIEW;
import static com.example.favoriteshowup.db.TvShowDatabaseContract.TvShowColumns.POSTER_PATH_STRING;
import static com.example.favoriteshowup.db.TvShowDatabaseContract.getColumnInt;
import static com.example.favoriteshowup.db.TvShowDatabaseContract.getColumnString;

public class TvShow implements Parcelable {
    private static final String TAG = "TvShow";

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel in) {
            return new TvShow(in);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };

    private int id;
    private String title; // title
    private String year; // release_date
    private int average; //vote_average
    private String overview; // overview
    private String photo; // poster path


    public TvShow() {

    }

    protected TvShow(Parcel in) {
        Log.d(TAG, "TvShow: running");
        id = in.readInt();
        title = in.readString();
        year = in.readString();
        average = in.readInt();
        overview = in.readString();
        photo = in.readString();
    }

    public TvShow(int id, String title, String photo, String overview) {
        this.id = id;
        this.title = title;
        this.photo = photo;
        this.overview = overview;
    }

    public TvShow(Cursor cursor) {
        this.id = getColumnInt(cursor, ID);
        this.title = getColumnString(cursor, NAME);
        this.photo = getColumnString(cursor, POSTER_PATH_STRING);
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

    public TvShow(JSONObject object){
        try {
            int id = object.getInt("id");
            String title = object.getString("original_name");
            String year = object.getString("first_air_date");
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
