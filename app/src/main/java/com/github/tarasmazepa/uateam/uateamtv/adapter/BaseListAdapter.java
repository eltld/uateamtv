package com.github.tarasmazepa.uateam.uateamtv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.common.base.Preconditions;

import java.util.List;

public abstract class BaseListAdapter<Item> extends BaseAdapter {
    protected final List<Item> items;
    protected final LayoutInflater layoutInflater;

    public BaseListAdapter(Context context, List<Item> items) {
        this.layoutInflater = LayoutInflater.from(context);
        this.items = Preconditions.checkNotNull(items);
    }

    public Context getContext(){
        return layoutInflater.getContext();
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
            convertView = inflateView(position, parent);
        }
        fillView(position, convertView, getItem(position));
        return convertView;
    }

    protected abstract void fillView(int position, View view, Item item);

    protected abstract View inflateView(int position, ViewGroup parent);
}
