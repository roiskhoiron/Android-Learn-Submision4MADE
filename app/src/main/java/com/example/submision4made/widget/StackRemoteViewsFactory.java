package com.example.submision4made.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.submision4made.R;
import com.example.submision4made.db.MovieHelper;
import com.example.submision4made.model.Movies;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "StackRemoteViewsFactory";
    private final ArrayList<String> mWidgetItems = new ArrayList<>();
    private final Context mContext;
    private ArrayList<Movies> listMovies = new ArrayList<>();
    private MovieHelper movieHelper;
    private Cursor cursor;

    public StackRemoteViewsFactory(Context context) {
        Log.d(TAG, "StackRemoteViewsFactory: running");
        this.mContext = context;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: running");
        getLoadData();
    }
    
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged: running");
        getLoadData();
    }

    private void getLoadData() {
        Log.d(TAG, "getLoadData: running");
        mWidgetItems.clear();
        listMovies.clear();

        movieHelper = new MovieHelper(this.mContext);
        movieHelper.open();
        if (!movieHelper.getAllMovies().isEmpty()) {
            listMovies.addAll(movieHelper.getAllMovies());
            Log.d(TAG, "getLoadData: added running");
        }
        movieHelper.close();

        for (Movies movie : listMovies) {
            mWidgetItems.add(movie.getPhoto());
            Log.d(TAG, "getLoadData: " + movie.getPhoto());
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: running");
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: running");
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Log.d(TAG, "getViewAt: running");
        /*RemoteViews rvs = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rvs.setImageViewBitmap(R.id.imageView, mWidgetItems.get(i));

        Bundle extras = new Bundle();
        extras.putInt(FampletMovieWidget.EXTRA_ITEMS, i);
        Intent intentFillIn = new Intent();
        intentFillIn.putExtras(extras);

        rvs.setOnClickFillInIntent(R.id.imageView, intentFillIn);
        return rvs;*/
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if (this.mWidgetItems.size() != 0) {
            Bitmap bitmap = null;
            try {
                bitmap = Glide.with(this.mContext)
                        .asBitmap()
                        .load(this.mWidgetItems.get(i))
                        .submit()
                        .get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rv.setImageViewBitmap(R.id.imageView, bitmap);
            Bundle extras = new Bundle();
            extras.putInt(FampletMovieWidget.EXTRA_ITEMS, i);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        Log.d(TAG, "getLoadingView: running");
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Log.d(TAG, "getViewTypeCount: running");
        return 1;
    }

    @Override
    public long getItemId(int i) {
        Log.d(TAG, "getItemId: running");
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        Log.d(TAG, "hasStableIds: running");
        return false;
    }
}
