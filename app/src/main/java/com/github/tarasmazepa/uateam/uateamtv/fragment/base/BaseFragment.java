package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.activity.MainActivity;
import com.github.tarasmazepa.uateam.uateamtv.analytics.Analytics;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.util.Fragments;

public abstract class BaseFragment extends TrackingFragment {
    protected static final String KEY_SECTION_NUMBER = "section_number";
    private static final String KEY_SHOW_RELOAD_BUTTON = "show_reload_button";

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected Button refreshButton;
    protected boolean loading;
    protected boolean showRefreshButton;

    protected abstract void loadResult();

    protected abstract View createView(LayoutInflater inflater, SwipeRefreshLayout swipeRefreshLayout, Bundle savedInstanceState);

    protected abstract Analytics.ScreenName getScreenName();

    protected abstract String getUrl();

    public static <T extends Fragment> T create(T fragment, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SECTION_NUMBER, position);
        return Fragments.setArguments(fragment, bundle);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).onSectionAttached(getPosition());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        startLoading();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_refresh, container, false);
        refreshButton = (Button) view.findViewById(R.id.refreshButton);
        if (savedInstanceState != null) {
            refreshButton.setVisibility(savedInstanceState.getBoolean(KEY_SHOW_RELOAD_BUTTON) ? View.VISIBLE : View.GONE);
        } else {
            refreshButton.setVisibility(showRefreshButton ? View.VISIBLE : View.GONE);
        }
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefreshLayout.addView(createView(inflater, swipeRefreshLayout, savedInstanceState), 0);
        swipeRefreshLayout.setColorSchemeResources(R.color.cherry);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoading();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                analytics.actionGeneral(Analytics.Action.REFRESH);
                startLoading();
            }
        });
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(loading);
            }
        }, 100);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.base_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_url:
                analytics.actionGeneral(Analytics.Action.OPEN_IN_BROWSER);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Uateamtv.absoluteUrl(getUrl()))));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        analytics.view(getScreenName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SHOW_RELOAD_BUTTON, showRefreshButton = refreshButton.getVisibility() == View.VISIBLE);
    }

    protected void startLoading() {
        if (refreshButton != null) {
            refreshButton.setVisibility(View.GONE);
        }
        if (!loading) {
            loading = true;
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(true);
            }
            loadResult();
        }
    }

    protected void onFinishLoading(boolean success) {
        loading = false;
        swipeRefreshLayout.setRefreshing(false);
        refreshButton.setVisibility(success ? View.GONE : View.VISIBLE);
    }

    public int getPosition() {
        return getArguments().getInt(KEY_SECTION_NUMBER);
    }
}
