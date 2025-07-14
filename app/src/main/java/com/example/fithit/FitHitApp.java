package com.example.fithit;

import android.app.Application;

public class FitHitApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationScheduler.scheduleNotifications(this);

        // Reset workoutCompletedToday for new day
        getSharedPreferences("workoutPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("workoutCompletedToday", false)
                .apply();
    }
}