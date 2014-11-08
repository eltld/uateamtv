package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import android.os.Bundle;

import com.github.tarasmazepa.uateam.uateamtv.analytics.Analytics;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.github.tarasmazepa.uateam.uateamtv.util.Fragments;

import java.util.List;

public class ReleaseListFragment extends LinkListFragment {
    public static final String KEY_LINK = "link";

    public static ReleaseListFragment create(String link) {
        return create(new ReleaseListFragment(), link, 0);
    }

    public static <T extends ReleaseListFragment> T create(T fragment, String link, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_LINK, link);
        bundle.putInt(KEY_SECTION_NUMBER, position);
        return Fragments.setArguments(fragment, bundle);
    }

    @Override
    protected void loadResult() {
        new ResultTask<Void, Void, List<Link>>() {
            @Override
            protected List<Link> produceData(Void... voids) throws Throwable {
                return transformToLinks(Uateamtv.page(getArguments().getString(KEY_LINK)).select(Uateamtv.SELECT));
            }

            @Override
            protected void onPostExecute(Result<List<Link>> result) {
                onFinishLoading(result);
            }
        }.execute();
    }

    @Override
    protected Analytics.ScreenName getScreenName() {
        return Analytics.ScreenName.RELEASE_LIST;
    }
}
