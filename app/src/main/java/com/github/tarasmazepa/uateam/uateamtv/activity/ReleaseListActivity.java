package com.github.tarasmazepa.uateam.uateamtv.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.fragment.base.ReleaseListFragment;

public class ReleaseListActivity extends BaseActivity {
    public static void start(Activity activity, String link, String title) {
        start(ReleaseListActivity.class, activity, link, title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_frame);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            getFragmentManager().beginTransaction().replace(R.id.container, ReleaseListFragment.create(getIntent().getStringExtra(ReleaseListFragment.KEY_LINK))).commit();
        }
    }
}
