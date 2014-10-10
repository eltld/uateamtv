package com.github.tarasmazepa.uateam.uateamtv.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.fragment.ReleaseFragment;
import com.google.common.base.Strings;

public class ReleaseActivity extends BaseActivity {
    public static void start(Activity activity, String link, String title, String subtitle) {
        if (Strings.isNullOrEmpty(subtitle)) {
            subtitle = "";
        } else {
            subtitle = " - " + subtitle;
        }
        start(ReleaseActivity.class, activity, link, title + subtitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_frame);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            getFragmentManager().beginTransaction().replace(R.id.container, new ReleaseFragment()).commit();
        }
    }
}
