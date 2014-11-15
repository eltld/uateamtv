package com.github.tarasmazepa.uateam.uateamtv.activity.base;

import android.app.Activity;
import android.os.Bundle;

import com.github.tarasmazepa.uateam.uateamtv.UateamtvApp;
import com.github.tarasmazepa.uateam.uateamtv.analytics.Analytics;

public class BaseActivity extends Activity {
    public Analytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = UateamtvApp.getAnalytics(this);
    }
}
