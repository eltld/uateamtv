package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.adapter.ListAdapter;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;

import java.util.List;

public abstract class ListFragment<Item extends Link> extends BaseFragment {
    protected ListView listView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListAdapter<Item> adapter;
    protected boolean loading;

    protected abstract ListAdapter.ViewFiller<Item> getViewFiller();

    protected abstract void loadResult();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        adapter = new ListAdapter<Item>(getActivity(), getViewFiller());
        startLoading();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.release_list, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading();
            }
        });
        swipeRefreshLayout.setRefreshing(loading);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClicked(adapter.getItem(position));
            }
        });
        return view;
    }

    protected void onItemClicked(Item item) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.link)));
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

    protected void onFinishLoading(Result<List<Item>> result) {
        swipeRefreshLayout.setRefreshing(false);
        loading = false;
        if (result.success) {
            adapter.reload(result.data);
        } else {
            Log.d(getClass().getSimpleName(), result.toString());
        }
    }
}
