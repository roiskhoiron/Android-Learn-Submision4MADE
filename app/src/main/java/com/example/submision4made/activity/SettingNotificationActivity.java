package com.example.submision4made.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.submision4made.AlarmReceiver;
import com.example.submision4made.R;

public class SettingNotificationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SettingNotificationActi";
    private AlarmReceiver alarmReceiver;
    private Switch swhReleaseToday, swhDailyReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: running");
        setContentView(R.layout.activity_setting_notification);
        setTitle(R.string.reminder_setting);

        swhReleaseToday = findViewById(R.id.switch_release_today_reminder);
        swhDailyReminder = findViewById(R.id.switch_daily_reminder);

        alarmReceiver = new AlarmReceiver();

        if (alarmReceiver.isAlarmSet(this, AlarmReceiver.TYPE_DAILY_REMINDER)) {
            swhDailyReminder.setChecked(true);
        } else {
            swhDailyReminder.setChecked(false);
        }

        if (alarmReceiver.isAlarmSet(this, AlarmReceiver.TYPE_RELEASE_TODAY_REMINDER)) {
            swhReleaseToday.setChecked(true);
        } else {
            swhReleaseToday.setChecked(false);
        }

        swhReleaseToday.setOnClickListener(this);
        swhDailyReminder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_daily_reminder:
                if (swhDailyReminder.isChecked()) {
                    String TEXT_DAILY_REMINDER = getString(R.string.daily_reminder_text);
                    Toast.makeText(this, getString(R.string.switch_daily_reminder_on_text), Toast.LENGTH_SHORT).show();

                    String TIME_DAILY_REMINDER = "07:00";
                    alarmReceiver.setDailyReminderAlarm(this, AlarmReceiver.TYPE_DAILY_REMINDER,
                            TIME_DAILY_REMINDER, TEXT_DAILY_REMINDER);
                } else {
                    Toast.makeText(this, getString(R.string.switch_daily_reminder_off_text), Toast.LENGTH_SHORT).show();
                    alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_DAILY_REMINDER);
                }
                break;
            case R.id.switch_release_today_reminder:
                if (swhReleaseToday.isChecked()) {
                    String TIME_RELEASE_TODAY_REMINDER = "08:00";
                    alarmReceiver.setDailyNewMovieReminderAlarm(this, AlarmReceiver.TYPE_RELEASE_TODAY_REMINDER,
                            TIME_RELEASE_TODAY_REMINDER);
                    Toast.makeText(this, getString(R.string.switch_release_today_reminder_on_text), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.switch_release_today_reminder_off_text), Toast.LENGTH_SHORT).show();
                    alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_RELEASE_TODAY_REMINDER);
                }
                break;
        }
    }
}