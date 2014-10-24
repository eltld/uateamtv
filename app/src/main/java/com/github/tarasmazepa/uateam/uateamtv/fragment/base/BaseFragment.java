package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.activity.MainActivity;
import com.github.tarasmazepa.uateam.uateamtv.util.Fragments;

public abstract class BaseFragment extends Fragment {
    protected static final String KEY_SECTION_NUMBER = "section_number";

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected boolean loading;

    protected abstract void loadResult();

    protected abstract View createView(LayoutInflater inflater, SwipeRefreshLayout swipeRefreshLayout, Bundle savedInstanceState);

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
        startLoading();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_refresh, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefreshLayout.addView(createView(inflater, swipeRefreshLayout, savedInstanceState), 0);
        swipeRefreshLayout.setColorSchemeResources(R.color.cherry);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading();
            }
        });
        swipeRefreshLayout.setRefreshing(loading);
        return view;
    }

    protected void startLoading() {
        if (!loading) {
            loading = true;
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(true);
            }
            loadResult();
        }
    }

    protected void onFinishLoading() {
        swipeRefreshLayout.setRefreshing(false);
        loading = false;
    }

    public int getPosition() {
        return getArguments().getInt(KEY_SECTION_NUMBER);
    }
}
