package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;

import com.github.tarasmazepa.uateam.uateamtv.analytics.Analytics;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.github.tarasmazepa.uateam.uateamtv.util.Fragments;

import java.util.List;

public class ReleaseListFragment extends LinkListFragment {
    public static final String KEY_LINK = "link";
    private static final int LOAD_MORE_STEP = 20;

    public static ReleaseListFragment create(String link) {
        return create(new ReleaseListFragment(), link, 0);
    }

    public static <T extends ReleaseListFragment> T create(T fragment, String link, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_LINK, link);
        bundle.putInt(KEY_SECTION_NUMBER, position);
        return Fragments.setArguments(fragment, bundle);
    }

    private String link;
    private int loadMoreIndex = LOAD_MORE_STEP;
    private boolean loadingMore;
    private boolean canLoadMore = true;

    @Override
    protected void loadResult() {
        new ResultTask<Void, Void, List<Link>>() {
            @Override
            protected List<Link> produceData(Void... voids) throws Throwable {
                return transformToLinks(Uateamtv.page(link).select(Uateamtv.SELECT));
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

    @Override
    protected String getUrl() {
        return link;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        link = getArguments().getString(KEY_LINK);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (canLoadMore && !loadingMore && firstVisibleItem + visibleItemCount == totalItemCount && adapter.getCount() > LOAD_MORE_STEP / 2) {
                    loadingMore = true;
                    new ResultTask<Void, Void, List<Link>>() {
                        @Override
                        protected List<Link> produceData(Void... voids) throws Throwable {
                            return transformToLinks(Uateamtv.page(link + "?start=" + loadMoreIndex).select(Uateamtv.SELECT));
                        }

                        @Override
                        protected void onPostExecute(Result<List<Link>> result) {
                            if (result.success) {
                                loadMoreIndex += LOAD_MORE_STEP;
                                adapter.add(result.data);
                                canLoadMore = !result.data.isEmpty();
                            }
                            loadingMore = false;
                        }
                    }.execute();
                }
            }
        });
    }
}
