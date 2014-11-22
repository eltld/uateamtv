package com.github.tarasmazepa.uateam.uateamtv.fragment;

import com.github.tarasmazepa.uateam.uateamtv.activity.ReleaseListActivity;
import com.github.tarasmazepa.uateam.uateamtv.analytics.Analytics;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.fragment.base.ListFragment;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import org.jsoup.nodes.Element;

import java.util.List;

public class SeriesFragment extends ListFragment<Link> {
    public static SeriesFragment create(int position) {
        return create(new SeriesFragment(), position);
    }

    @Override
    protected void loadResult() {
        new ResultTask<Void, Void, List<Link>>() {
            @Override
            protected List<Link> produceData(Void... voids) throws Throwable {
                return Lists.newArrayList(Collections2.transform(Uateamtv.home().select("div#ja-col1 div.module:eq(0) table a"), new Function<Element, Link>() {
                    @Override
                    public Link apply(Element input) {
                        return new Link(input.text(), input.attr("href"));
                    }
                }));
            }

            @Override
            protected void onPostExecute(Result<List<Link>> result) {
                onFinishLoading(result);
            }
        }.execute();
    }

    @Override
    protected Analytics.ScreenName getScreenName() {
        return Analytics.ScreenName.SERIES;
    }

    @Override
    protected String getUrl() {
        return "";
    }

    @Override
    protected void onItemClicked(Link link) {
        ReleaseListActivity.start(getActivity(), link.link, link.title);
    }
}
