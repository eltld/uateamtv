package com.github.tarasmazepa.uateam.uateamtv.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class FindViewHolder extends RecyclerView.ViewHolder implements FindView.ViewSource {
    private static final int ROOT_VIEW_ID = 0;

    protected final Map<Integer, View> views;

    public FindViewHolder(View view) {
        this(view, R.id.line1, R.id.line2, R.id.line3, R.id.image);
    }

    public FindViewHolder(View view, int... ids) {
        super(view);
        ImmutableMap.Builder<Integer, View> builder = ImmutableMap.builder();
        builder.put(ROOT_VIEW_ID, view);
        for (int id : ids) {
            builder.put(id, view.findViewById(id));
        }
        views = builder.build();
    }

    @Override
    public View get(int id) {
        return views.get(id);
    }
}
