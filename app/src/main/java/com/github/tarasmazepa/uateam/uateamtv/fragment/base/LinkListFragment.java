package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import com.github.tarasmazepa.uateam.uateamtv.adapter.LinkViewFiller;
import com.github.tarasmazepa.uateam.uateamtv.adapter.ListAdapter;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import org.jsoup.nodes.Element;

import java.util.Collection;
import java.util.List;

public abstract class LinkListFragment extends ListFragment<Link> {
    @Override
    protected ListAdapter.ViewFiller<Link> getViewFiller() {
        return new LinkViewFiller<Link>();
    }

    public List<Link> transformToLinks(Collection<Element> elements) {
        return Lists.newArrayList(Collections2.transform(elements, new Function<Element, Link>() {
            @Override
            public Link apply(Element input) {
                return new Link(input.text(), input.attr("abs:href"));
            }
        }));
    }
}
