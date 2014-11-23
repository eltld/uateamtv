package com.github.tarasmazepa.uateam.uateamtv.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class Analytics {
    public enum ScreenName {
        FRESH("Fresh"), SERIES("Series"), MOVIES("Movies"), CARTOONS("Cartoons"),
        RELEASE_LIST("Release list"), RELEASE("Release"), VIDEO("Video");
        public final String name;

        private ScreenName(String name) {
            this.name = name;
        }
    }

    public enum Category {
        GENERAL("General");
        public final String name;

        private Category(String name) {
            this.name = name;
        }
    }

    public enum Action {
        WATCH_VIDEO_OTHER_APP("Watch video other application"), OPEN_IN_BROWSER("Open in browser"),
        SUPPORT("Support"), REFRESH("Refresh"), EMAIL_DEVELOPER("Email developer"),
        LOAD_MORE("Load more");
        public final String name;

        private Action(String name) {
            this.name = name;
        }
    }

    public final Tracker tracker;

    public Analytics(Tracker tracker) {
        this.tracker = tracker;
        tracker.enableAdvertisingIdCollection(true);
    }

    public void view(ScreenName screenName) {
        tracker.setScreenName(screenName.name);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void action(Category category, Action action) {
        tracker.send(new HitBuilders.EventBuilder().setCategory(category.name).setAction(action.name).build());
    }

    public void actionGeneral(Action action) {
        action(Category.GENERAL, action);
    }
}
