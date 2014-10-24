package com.github.tarasmazepa.uateam.uateamtv.adapter;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.github.tarasmazepa.uateam.uateamtv.view.FindView;

public class ViewFiller<Item extends Link> {
    public void fillView(FindView find, Item item) {
        find.text(R.id.line2).setText(item.title);
    }
}