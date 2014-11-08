package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import android.app.Activity;
import android.app.Fragment;

import com.github.tarasmazepa.uateam.uateamtv.UateamtvApp;
import com.github.tarasmazepa.uateam.uateamtv.analytics.Analytics;

public class TrackingFragment extends Fragment{
    protected Analytics analytics;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        analytics = UateamtvApp.getAnalytics(activity);
    }
}
