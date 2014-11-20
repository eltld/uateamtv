package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import com.github.tarasmazepa.uateam.uateamtv.activity.base.BaseChildActivity;
import com.github.tarasmazepa.uateam.uateamtv.adapter.ViewFiller;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import org.jsoup.nodes.Element;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LinkListFragment extends ListFragment<Link> {
    private static final Pattern PATTERN = Pattern.compile("Повний (\\d) сезон");
    private static final Predicate<Link> NOT_SERIES_BUNDLE_LINK = new Predicate<Link>() {
        @Override
        public boolean apply(Link input) {
            return !PATTERN.matcher(input.title).matches();
        }
    };

    @Override
    protected ViewFiller<Link> getViewFiller() {
        return new ViewFiller<Link>();
    }

    public List<Link> transformToLinks(Collection<Element> elements) {
        final String seriesTitle = BaseChildActivity.getTitle(getActivity());
        return Lists.newArrayList(Collections2.filter(Collections2.transform(elements, new Function<Element, Link>() {
            int seasonNumber = 0;

            @Override
            public Link apply(Element input) {
                Link link = new Link(input.text(), input.attr("href"));
                Matcher matcher = PATTERN.matcher(link.title);
                if (matcher.matches()) {
                    Integer integer = Ints.tryParse(matcher.group(1));
                    if (integer != null) {
                        seasonNumber = integer.intValue();
                    }
                }
                link.season = seasonNumber;
                try {
                    link.episode = Ints.tryParse(input.parent().parent().child(0).child(0).text()).intValue();
                } catch (Exception e) {
                    // eating this one
                }
                link.groupTitle = seriesTitle;
                return link;
            }
        }), NOT_SERIES_BUNDLE_LINK));
    }
}
