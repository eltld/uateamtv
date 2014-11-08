package com.github.tarasmazepa.uateam.uateamtv.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class Analytics {
    public enum ScreenName {
        FRESH("Fresh"), SERIES("Series"), MOVIES("Movies"), CARTOONS("Cartoons"),
        RELEASE_LIST("Release list"), RELEASE("Release");
        public final String name;

        private ScreenName(String name) {
            this.name = name;
        }
    }
    public enum Action {
        WATCH_VIDEO("Watch video"), OPEN_IN_BROWSER("Open in browser"), SUPPORT("Support"), REFRESH("refresh");
        public final String name;

        private Action(String name) {
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

    public void action(Action action) {
        tracker.send(new HitBuilders.EventBuilder().setAction(action.name).build());
    }
}
