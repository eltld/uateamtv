package com.github.tarasmazepa.uateam.uateamtv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.github.tarasmazepa.uateam.uateamtv.view.FindView;
import com.google.common.collect.Lists;

import java.util.List;

public class ListAdapter<Item extends Link> extends BaseAdapter {
    protected final List<Item> items;
    protected final LayoutInflater layoutInflater;
    private final ViewFiller<Item> filler;

    public ListAdapter(Context context, ViewFiller<Item> filler) {
        this.items = Lists.newArrayList();
        this.layoutInflater = LayoutInflater.from(context);
        this.filler = filler;
    }

    public void reload(List<Item> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = FindView.holdViewInTags(layoutInflater.inflate(R.layout.release_list_item, parent, false), R.id.line1, R.id.line2, R.id.line3, R.id.image);
        }
        filler.fillView(FindView.inTag(convertView), getItem(position));
        return convertView;
    }
}
