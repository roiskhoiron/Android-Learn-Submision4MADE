package com.example.favoriteshowup.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.favoriteshowup.BuildConfig;
import com.example.favoriteshowup.Config;
import com.example.favoriteshowup.model.TvShow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class TvShowsViewModel extends ViewModel {
    private static final String TAG = "TvShowsViewModel";

    private static final String URL_FULL_TVSHOW = Config.URL_MOVIE_AND_TV_SHOW_BASE
            + Config.URL_TV_SHOW_DISCOVER
            + "?api_key="
            + BuildConfig.API_KEY +
            "&language=en-US";

    private static final String URL_FILTERED_TV_SHOW = Config.URL_MOVIE_AND_TV_SHOW_BASE
            + Config.URL_TV_SHOW_SEARCH
            + "?api_key="
            + BuildConfig.API_KEY +
            "&language=en-US&query=";

    private MutableLiveData<ArrayList<TvShow>> listTvShow = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TvShow>> listFilteredTvShows = new MutableLiveData<>();

    public void setTvShows() {
        Log.d(TAG, "setTvShows: running");
        AsyncHttpClient client = new AsyncHttpClient();

        final ArrayList<TvShow> listItems = new ArrayList<>();

        client.get(URL_FULL_TVSHOW, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "onSuccess: running");
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tvShow = list.getJSONObject(i);
                        TvShow tvShowItems = new TvShow(tvShow);
                        listItems.add(tvShowItems);
                    }
                    listTvShow.postValue(listItems);
                    Log.d("sukses", "onSuccess: Running");
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "onFailure: running");
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public void setTvShows(String searchQry) {
        Log.d(TAG, "setTvShows: running");
        AsyncHttpClient client = new AsyncHttpClient();

        final ArrayList<TvShow> listItems = new ArrayList<>();

        String url = URL_FILTERED_TV_SHOW + searchQry;

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "onSuccess: running");
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tvShow = list.getJSONObject(i);
                        TvShow tvShowItems = new TvShow(tvShow);
                        listItems.add(tvShowItems);
                    }
                    listFilteredTvShows.postValue(listItems);

                    Log.d("sukses", "onSuccess: Running");
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "onFailure: running");
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public LiveData<ArrayList<TvShow>> getTvShows() {
        Log.d(TAG, "getMovies: running");
        return listTvShow;
    }

    public LiveData<ArrayList<TvShow>> getFilteredTvShows() {
        Log.d(TAG, "getFilteredTvShows: running");
        return listFilteredTvShows;
    }
}
