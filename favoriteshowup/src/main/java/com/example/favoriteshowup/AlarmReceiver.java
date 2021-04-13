package com.example.favoriteshowup;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.favoriteshowup.model.Movies;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    public static final String TYPE_DAILY_REMINDER = "DailyReminderAlarm"; //TYPE_ONE_TIME
    public static final String TYPE_RELEASE_TODAY_REMINDER = "ReleaseTodayAlarm"; // TYPE_REPEATING
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    private final static int ID_DAILY_REMINDER = 100;
    private final static int ID_RELEASE_TODAY_REMINDER = 101;

    public AlarmReceiver() {
        Log.d(TAG, "AlarmReceiver: running");

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: running");
        final String type = intent.getStringExtra(EXTRA_TYPE);
        final Context thisContext = context;

        if (type.equals(TYPE_RELEASE_TODAY_REMINDER)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date currentTime = Calendar.getInstance().getTime();
            String todayDate = dateFormat.format(currentTime);

            String url = Config.URL_MOVIE_AND_TV_SHOW_BASE
                    + Config.URL_MOVIE_DISCOVER
                    + "?api_key="
                    + BuildConfig.API_KEY
                    + "&primary_release_date.gte="
                    + todayDate
                    + "&primary_release_date.lte="
                    + todayDate;

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d(TAG, "onSuccess: running");
                    ArrayList<Movies> listItems = new ArrayList<>();

                    String result = new String(responseBody);
                    try {
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list =  responseObject.getJSONArray("results");

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject movie = list.getJSONObject(i);
                            Movies movieItem = new Movies(movie);
                            listItems.add(movieItem);
                        }

                        String message = listItems.size() + " New Movie Today: ";

                        for (Movies item: listItems) {
                            if (item.getTitle() == listItems.get(listItems.size() - 1).getTitle()) {
                                message = message + item.getTitle();
                            } else {
                                message = message + item.getTitle() + ", ";
                            }
                        }

                        showToast(thisContext, type, message);

                        showAlarmNotification(thisContext, type, message, ID_RELEASE_TODAY_REMINDER);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("ERROR", error.toString());
                }
            });

        } else {
            String message = intent.getStringExtra(EXTRA_MESSAGE);

            String title = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? TYPE_DAILY_REMINDER : TYPE_RELEASE_TODAY_REMINDER;
            int notifId = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? ID_DAILY_REMINDER : ID_RELEASE_TODAY_REMINDER;

            showToast(context, title, message);

            showAlarmNotification(context, title, message, notifId);
        }
    }

    private void showToast(Context context, String title, String message) {
        Log.d(TAG, "showToast: running");
        Toast.makeText(context, title + " : " + message, Toast.LENGTH_LONG).show();
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        Log.d(TAG, "showAlarmNotification: running");
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context,
                notifId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_filter_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(notificationPendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    public void setDailyReminderAlarm(Context context, String type, String time, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void setDailyNewMovieReminderAlarm(Context context, String type, String time) {
        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TYPE, type);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                ID_RELEASE_TODAY_REMINDER, intent, 0);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? ID_DAILY_REMINDER : ID_RELEASE_TODAY_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public boolean isAlarmSet(Context context, String type) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? ID_DAILY_REMINDER : ID_RELEASE_TODAY_REMINDER;

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }
}
