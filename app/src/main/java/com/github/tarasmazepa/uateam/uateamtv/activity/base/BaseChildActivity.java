package com.github.tarasmazepa.uateam.uateamtv.activity.base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class BaseChildActivity extends BaseActivity {
    public static final String KEY_LINK = "link";
    private static final String KEY_TITLE = "title";

    public static void start(Class<? extends BaseActivity> activityClass, Activity activity, String link, String title) {
        Intent intent = new Intent(activity, activityClass);
        intent.putExtra(KEY_LINK, link);
        intent.putExtra(KEY_TITLE, title);
        activity.startActivity(intent);
    }

    public static String getTitle(Activity activity) {
        return getStringExtra(activity, KEY_TITLE);
    }

    public static String getLink(Activity activity) {
        return getStringExtra(activity, KEY_LINK);
    }

    private static String getStringExtra(Activity activity, String key) {
        return activity.getIntent().getStringExtra(key);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getTitle(this));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
