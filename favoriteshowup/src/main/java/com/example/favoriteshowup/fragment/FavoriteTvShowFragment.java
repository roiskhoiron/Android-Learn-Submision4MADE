package com.example.favoriteshowup.fragment;

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

import com.example.favoriteshowup.R;
import com.example.favoriteshowup.adapter.FavoriteTvShowAdapter;
import com.example.favoriteshowup.db.TvShowDatabaseContract;
import com.example.favoriteshowup.model.TvShow;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.favoriteshowup.db.MappingHelper.mapCursorTvToArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvShowFragment extends Fragment implements LoadTvCallback {
    private static final String TAG = "FavoriteTvShowFragment";
    private static final String EXTRA_STATE_TVSHOW = "extra_state_tvshow";
    private FavoriteTvShowAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvFavoriteFragmentTvShows = view.findViewById(R.id.rv_fragment_favorite_tv_show);
        adapter = new FavoriteTvShowAdapter(view.getContext());
        rvFavoriteFragmentTvShows.setLayoutManager(new LinearLayoutManager(view.getContext()));

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper());
        FavoriteTvShowFragment.DataObserver favTvObserver = new FavoriteTvShowFragment.DataObserver(handler, getContext());

        if (getActivity() != null) {
            getActivity().getContentResolver().registerContentObserver(TvShowDatabaseContract.TvShowColumns.CONTENT_URI, true, favTvObserver);
        }

        rvFavoriteFragmentTvShows.setAdapter(adapter);

        if (savedInstanceState == null) {
            new FavoriteTvShowFragment.LoadTvAsync(getContext(), this).execute();
        } else {
            ArrayList<TvShow> tvShowItems = savedInstanceState.getParcelableArrayList(EXTRA_STATE_TVSHOW);
            if (tvShowItems != null) {
                adapter.setData(tvShowItems);
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
        outState.putParcelableArrayList(EXTRA_STATE_TVSHOW, adapter.getMovies());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: runnning");
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadTvAsync(getContext(), this).execute();
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
        ArrayList<TvShow> tvShowItems = mapCursorTvToArrayList(cursor);
        if (tvShowItems.size() > 0) {
            adapter.setData(tvShowItems);
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
    }

    private static class LoadTvAsync extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadTvCallback> weakCallback;

        private LoadTvAsync(Context context, LoadTvCallback callback) {
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
            return context.getContentResolver().query(TvShowDatabaseContract.TvShowColumns.CONTENT_URI, null, null, null, null);
        }
    }
}

interface LoadTvCallback {
    void preExecute();

    void postExecute(Cursor cursor);
}
