package com.github.tarasmazepa.uateam.uateamtv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.fragment.base.ReleaseListFragment;

public class ReleaseListActivity extends BaseActivity {
    public static void start(Activity activity, String link) {
        start(ReleaseListActivity.class, activity, link);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_frame);
        getFragmentManager().beginTransaction().add(R.id.container, ReleaseListFragment.create(getIntent().getStringExtra(ReleaseListFragment.KEY_LINK))).commit();
    }
}
