package com.github.tarasmazepa.uateam.uateamtv;

import android.app.Activity;
import android.app.Application;

import com.github.tarasmazepa.uateam.uateamtv.analytics.Analytics;
import com.google.android.gms.analytics.GoogleAnalytics;

public class UateamtvApp extends Application {
    public Analytics analytics;

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = new Analytics(GoogleAnalytics.getInstance(this).newTracker(R.xml.tracker));
    }

    public static UateamtvApp getApp(Activity activity) {
        return (UateamtvApp) activity.getApplication();
    }

    public static Analytics getAnalytics(Activity activity) {
        return getApp(activity).analytics;
    }
}
