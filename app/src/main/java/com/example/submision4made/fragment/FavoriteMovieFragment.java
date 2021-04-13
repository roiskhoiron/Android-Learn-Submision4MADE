package com.example.submision4made.fragment;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submision4made.R;
import com.example.submision4made.adapter.FavoriteMoviesAdapter;
import com.example.submision4made.model.Movies;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.submision4made.db.MappingHelper.mapCursorToArrayList;
import static com.example.submision4made.db.MovieDatabaseContract.MovieColumns.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment implements LoadMovieCallback {
    private static final String TAG = "FavoriteMovieFragment";
    private static final String EXTRA_STATE_MOVIE = "extra_state_movie";
    private FavoriteMoviesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: running");
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: running");

        RecyclerView rvFavoriteFragmentMovies = view.findViewById(R.id.rv_fragment_favorite_movie);
        adapter = new FavoriteMoviesAdapter(view.getContext());
        rvFavoriteFragmentMovies.setLayoutManager(new LinearLayoutManager(view.getContext()));


        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver favoriteMovieObserver = new DataObserver(handler, getContext());

        if (getActivity() != null) {
            getActivity().getContentResolver().registerContentObserver(CONTENT_URI, true, favoriteMovieObserver);
        }
        
        rvFavoriteFragmentMovies.setAdapter(adapter);
        
        if (savedInstanceState == null) {
            new LoadMovieAsync(getContext(), this).execute();
        } else {
            ArrayList<Movies> moviesItems = savedInstanceState.getParcelableArrayList(EXTRA_STATE_MOVIE);
            if (moviesItems != null) {
                adapter.setData(moviesItems);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE_MOVIE, adapter.getMovies());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: running");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: running");
        new LoadMovieAsync(getContext(), this).execute();
    }

    @Override
    public void preExecute() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    @Override
    public void postExecute(Cursor cursor) {
        ArrayList<Movies> moviesItems = mapCursorToArrayList(cursor);
        if (moviesItems.size() > 0) {
            adapter.setData(moviesItems);
        } else {
            Toast.makeText(getActivity(), "Data tidak ada", Toast.LENGTH_SHORT).show();
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;

        private final WeakReference<LoadMovieCallback> weakCallback;

        private LoadMovieAsync(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

    }

}

interface LoadMovieCallback {
    void preExecute();

    void postExecute(Cursor cursor);
}
