package com.example.submision4made.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.submision4made.BuildConfig;
import com.example.submision4made.Config;
import com.example.submision4made.model.Movies;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class MoviesViewModel extends ViewModel {
    private static final String TAG = "MoviesViewModel";

    private static final String URL_FULL_MOVIES = Config.URL_MOVIE_AND_TV_SHOW_BASE
            + Config.URL_MOVIE_DISCOVER
            + "?api_key="
            + BuildConfig.API_KEY +
            "&language=en-US";

    private static final String URL_FILTERED_MOVIES = Config.URL_MOVIE_AND_TV_SHOW_BASE
            + Config.URL_MOVIE_SEARCH
            + "?api_key="
            + BuildConfig.API_KEY +
            "&language=en-US&query=";


    private MutableLiveData<ArrayList<Movies>> listMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movies>> listFilteredMovies = new MutableLiveData<>();

    public void setMovies() {
        Log.d(TAG, "setMovies: running");
        AsyncHttpClient client = new AsyncHttpClient();

        final ArrayList<Movies> listItems = new ArrayList<>();

        client.get(URL_FULL_MOVIES, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "onSuccess: running");
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movies = list.getJSONObject(i);
                        Movies movieItems = new Movies(movies);
                        listItems.add(movieItems);
                    }
                    listMovies.postValue(listItems);
                    Log.d("sukses", "onSuccess: ");
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

    public void setMovies(String searchQry) {
        Log.d(TAG, "setMovies: running");

        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movies> listItems = new ArrayList<>();

        String url = URL_FILTERED_MOVIES + searchQry;

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "onSuccess: running");
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movies = list.getJSONObject(i);
                        Movies movieItems = new Movies(movies);
                        listItems.add(movieItems);
                    }

                    listFilteredMovies.postValue(listItems);

                    Log.d("sukses", "onSuccess: ");
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

    public LiveData<ArrayList<Movies>> getMovies() {
        Log.d(TAG, "getMovies: running");
        return listMovies;
    }
    public LiveData<ArrayList<Movies>> getFilteredMovies() {
        Log.d(TAG, "getFilteredMovies: running");
        return listFilteredMovies;
    }
}
