package com.github.tarasmazepa.uateam.uateamtv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ListAdapter<Item> extends BaseListAdapter<Item> {
    public interface ViewFiller<Item> {
        void fillView(int position, View view, Item item);

        View inflateView(LayoutInflater layoutInflater, int position, ViewGroup parent);
    }

    private final ViewFiller<Item> filler;

    public ListAdapter(Context context, ViewFiller<Item> filler) {
        super(context);
        this.filler = filler;
    }

    public ListAdapter(Context context, ViewFiller<Item> filler, List<Item> items) {
        super(context, items);
        this.filler = filler;
    }

    @Override
    protected void fillView(int position, View view, Item item) {
        filler.fillView(position, view, item);
    }

    @Override
    protected View inflateView(int position, ViewGroup parent) {
        return filler.inflateView(layoutInflater, position, parent);
    }
}
