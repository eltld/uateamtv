package com.github.tarasmazepa.uateam.uateamtv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.view.FindView;

public abstract class BaseViewFiller<Item> implements ListAdapter.ViewFiller<Item> {
    @Override
    public View inflateView(LayoutInflater layoutInflater, int position, ViewGroup parent) {
        return FindView.holdViewInTags(layoutInflater.inflate(R.layout.release_list_item, parent, false), R.id.line1, R.id.line2, R.id.line3, R.id.image, R.id.color1, R.id.color2, R.id.color3, R.id.color4, R.id.color5, R.id.color6);
    }
}