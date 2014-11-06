package com.github.tarasmazepa.uateam.uateamtv;

import android.app.Activity;
import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class UateamtvApp extends Application {
    private Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.tracker);
    }

    public static UateamtvApp getApp(Activity activity) {
        return (UateamtvApp) activity.getApplication();
    }

    public static Tracker getTracker(Activity activity) {
        return getApp(activity).tracker;
    }
}
