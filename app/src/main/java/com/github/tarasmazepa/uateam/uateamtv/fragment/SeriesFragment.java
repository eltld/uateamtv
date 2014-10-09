package com.github.tarasmazepa.uateam.uateamtv.fragment;

import com.github.tarasmazepa.uateam.uateamtv.activity.ReleaseListActivity;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.fragment.base.LinkListFragment;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;

import java.util.List;

public class SeriesFragment extends LinkListFragment {
    public static SeriesFragment create(int position) {
        return create(new SeriesFragment(), position);
    }

    @Override
    protected void loadResult() {
        new ResultTask<Void, Void, List<Link>>() {
            @Override
            protected List<Link> produceData(Void... voids) throws Throwable {
                return transformToLinks(Uateamtv.home().select("div#ja-col1 div.module:eq(0) table a"));
            }

            @Override
            protected void onPostExecute(Result<List<Link>> result) {
                onFinishLoading(result);
            }
        }.execute();
    }

    @Override
    protected void onItemClicked(Link link) {
        ReleaseListActivity.start(getActivity(), link.link, link.title);
    }
}
