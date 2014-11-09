package com.github.tarasmazepa.uateam.uateamtv.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class BaseActivity extends Activity {
    public static final String KEY_LINK = "link";
    public static final String KEY_TITLE = "title";

    public static void start(Class<? extends BaseActivity> activityClass, Activity activity, String link, String title) {
        Intent intent = new Intent(activity, activityClass);
        intent.putExtra(KEY_LINK, link);
        intent.putExtra(KEY_TITLE, title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra(KEY_TITLE));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
