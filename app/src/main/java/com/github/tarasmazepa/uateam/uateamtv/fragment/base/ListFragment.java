package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.activity.BaseActivity;
import com.github.tarasmazepa.uateam.uateamtv.activity.ReleaseActivity;
import com.github.tarasmazepa.uateam.uateamtv.adapter.ListAdapter;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;

import java.util.List;

public abstract class ListFragment<Item extends Link> extends BaseFragment {
    protected ListView listView;
    protected ListAdapter<Item> adapter;

    protected abstract ListAdapter.ViewFiller<Item> getViewFiller();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ListAdapter<Item>(getActivity(), getViewFiller());
    }

    @Override
    protected View createView(LayoutInflater inflater, SwipeRefreshLayout container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.release_list, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClicked(adapter.getItem(position));
            }
        });
        return view;
    }

    protected void onItemClicked(Item item) {
        ReleaseActivity.start(getActivity(), item.link, item.title, getActivity().getIntent().getStringExtra(BaseActivity.KEY_TITLE));
    }

    protected void onFinishLoading(Result<List<Item>> result) {
        onFinishLoading();
        if (result.success) {
            adapter.reload(result.data);
        } else {
            Log.d(getClass().getSimpleName(), result.toString());
        }
    }
}
