package com.github.tarasmazepa.uateam.uateamtv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.view.FindViewHolder;
import com.google.common.collect.Lists;

import java.util.List;

public class RecyclerAdapter<Item> extends RecyclerView.Adapter<FindViewHolder> {
    protected final List<Item> list;

    public RecyclerAdapter() {
        list = Lists.newArrayList();
    }

    @Override
    public FindViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new FindViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.release_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(FindViewHolder findViewHolder, int i) {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
