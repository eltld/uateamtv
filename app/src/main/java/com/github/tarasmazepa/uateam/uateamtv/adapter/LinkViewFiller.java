package com.github.tarasmazepa.uateam.uateamtv.adapter;

import android.view.View;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.github.tarasmazepa.uateam.uateamtv.view.FindView;

public class LinkViewFiller<Item extends Link> extends BaseViewFiller<Item> {
    @Override
    public void fillView(int position, View view, Item item) {
        FindView findView = FindView.inTag(view);
        findView.text(R.id.line2).setText(item.title);
    }
}
