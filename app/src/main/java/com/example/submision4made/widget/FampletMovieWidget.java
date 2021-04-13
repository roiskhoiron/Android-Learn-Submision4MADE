package com.example.submision4made.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.submision4made.R;

/**
 * Implementation of App Widget functionality.
 */
public class FampletMovieWidget extends AppWidgetProvider {
    private static final String TAG = "FampletMovieWidget";

    private static final String TOAST_ACTION = "com.example.submisision4made.TOAST_ACTION";
    public static final String EXTRA_ITEMS = "com.example.submisision4made.EXTRA_ITEMS";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(TAG, "updateAppWidget: running");

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.famplet_movie_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.appwidget_text);

        // Instruct the widget manager to update the widget
        Intent toastIntent = new Intent(context, FampletMovieWidget.class);
        toastIntent.setAction(FampletMovieWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate: running");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive: running");
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                int viewIndex = intent.getIntExtra(EXTRA_ITEMS, 0);
                Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled: running");
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled: running");
    }
}

