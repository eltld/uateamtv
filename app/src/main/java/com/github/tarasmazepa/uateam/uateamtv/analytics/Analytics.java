package com.github.tarasmazepa.uateam.uateamtv.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class Analytics {
    public enum ScreenName {
        FRESH("Fresh"), SERIES("Series"), MOVIES("Movies"), CARTOONS("Cartoons"),
        RELEASE_LIST("release list"), RELEASE("release");
        public final String name;

        private ScreenName(String name) {
            this.name = name;
        }
    }

    public final Tracker tracker;

    public Analytics(Tracker tracker) {
        this.tracker = tracker;
    }

    public void viewScreen(ScreenName screenName){
        tracker.setScreenName(screenName.name);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void viewApp() {
        tracker.send(new HitBuilders.AppViewBuilder().build());
    }
}
