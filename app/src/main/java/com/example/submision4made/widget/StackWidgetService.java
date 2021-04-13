package com.example.submision4made.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class StackWidgetService extends RemoteViewsService {
    private static final String TAG = "StackWidgetService";
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory: running");
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}