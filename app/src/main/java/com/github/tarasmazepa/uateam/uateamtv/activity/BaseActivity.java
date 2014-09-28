package com.github.tarasmazepa.uateam.uateamtv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class BaseActivity extends Activity{
    protected static final String KEY_LINK = "link";

    public static void start(Class<? extends BaseActivity> activityClass, Activity activity, String link) {
        Intent intent = new Intent(activity, activityClass);
        intent.putExtra(KEY_LINK, link);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
