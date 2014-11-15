package com.github.tarasmazepa.uateam.uateamtv.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.UateamtvApp;
import com.github.tarasmazepa.uateam.uateamtv.analytics.Analytics;

public class BaseActivity extends Activity {
    public Analytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = UateamtvApp.getAnalytics(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.base_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_email_developer:
                analytics.actionGeneral(Analytics.Action.EMAIL_DEVELOPER);
                //TODO: start email app
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
