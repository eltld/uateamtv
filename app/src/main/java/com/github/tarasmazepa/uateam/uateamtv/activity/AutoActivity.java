package com.github.tarasmazepa.uateam.uateamtv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.fragment.base.AutoFragment;

public class AutoActivity extends Activity {
    public static void start(Activity activity, String link) {
        Intent intent = new Intent(activity, AutoActivity.class);
        intent.putExtra(AutoFragment.KEY_LINK, link);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_frame);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().add(R.id.container, AutoFragment.create(getIntent().getStringExtra(AutoFragment.KEY_LINK))).commit();
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
