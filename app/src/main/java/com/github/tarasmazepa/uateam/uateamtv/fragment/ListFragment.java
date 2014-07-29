package com.github.tarasmazepa.uateam.uateamtv.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.adapter.ListAdapter;

import java.util.List;

public abstract class ListFragment<Item> extends Fragment {
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListAdapter<Item> listAdapter;
    private boolean loading;

    protected abstract ListAdapter.ViewFiller<Item> getViewFiller();
    protected abstract void loadItems();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new ListAdapter<Item>(getActivity(), getViewFiller());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.release_list, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        listView.setAdapter(listAdapter);
        return view;
    }
}
