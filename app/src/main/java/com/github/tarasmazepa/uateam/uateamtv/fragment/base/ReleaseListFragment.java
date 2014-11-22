package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.activity.base.BaseChildActivity;
import com.github.tarasmazepa.uateam.uateamtv.analytics.Analytics;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.github.tarasmazepa.uateam.uateamtv.util.Fragments;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseListFragment extends ListFragment<Link> {
    public static final String KEY_LINK = "link";
    private static final int LOAD_MORE_STEP = 20;
    private static final Pattern PATTERN_SEASON_CAPTION = Pattern.compile("Сезон (\\d)");
    private static final Pattern PATTERN = Pattern.compile("Повний (\\d) сезон");
    private static final Predicate<Link> NOT_SERIES_BUNDLE_LINK = new Predicate<Link>() {
        @Override
        public boolean apply(Link input) {
            return !PATTERN.matcher(input.title).matches();
        }
    };

    public static ReleaseListFragment create(String link) {
        return create(new ReleaseListFragment(), link, 0);
    }

    public static <T extends ReleaseListFragment> T create(T fragment, String link, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_LINK, link);
        bundle.putInt(KEY_SECTION_NUMBER, position);
        return Fragments.setArguments(fragment, bundle);
    }

    private ProgressBar progressBar;
    private String link;
    private int loadMoreIndex = LOAD_MORE_STEP;
    private boolean loadingMore;
    private boolean canLoadMore = true;

    @Override
    protected void loadResult() {
        new ResultTask<Void, Void, List<Link>>() {
            @Override
            protected List<Link> produceData(Void... voids) throws Throwable {
                return loadLinkList(link);
            }

            @Override
            protected void onPostExecute(Result<List<Link>> result) {
                onFinishLoading(result);
            }
        }.execute();
    }

    @Override
    protected void onFinishLoading(Result<List<Link>> result) {
        super.onFinishLoading(result);
        if (result.success) {
            canLoadMore = true;
            loadMoreIndex = LOAD_MORE_STEP;
        }
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
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (canLoadMore && !loadingMore && firstVisibleItem + visibleItemCount == totalItemCount && adapter.getCount() > LOAD_MORE_STEP / 2) {
                    loadingMore = true;
                    progressBar.setVisibility(View.VISIBLE);
                    listView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.smoothScrollToPosition(adapter.getCount());
                        }
                    }, 150);
                    new ResultTask<Void, Void, List<Link>>() {
                        @Override
                        protected List<Link> produceData(Void... voids) throws Throwable {
                            return loadLinkList(link + "?start=" + loadMoreIndex);
                        }

                        @Override
                        protected void onPostExecute(Result<List<Link>> result) {
                            if (result.success) {
                                loadMoreIndex += LOAD_MORE_STEP;
                                adapter.add(result.data);
                                canLoadMore = !result.data.isEmpty();
                            }
                            progressBar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 150);
                            loadingMore = false;
                        }
                    }.execute();
                }
            }
        });
    }

    private List<Link> loadLinkList(String url) throws IOException {
        final String seriesTitle = BaseChildActivity.getTitle(getActivity());
        return Lists.newArrayList(Collections2.filter(Collections2.transform(Uateamtv.page(url).select("div#ja-content table.adminlist tbody tr"),
                new Function<Element, Link>() {
                    int seasonNumber = 0;

                    @Override
                    public Link apply(Element input) {
                        Matcher matcher = PATTERN_SEASON_CAPTION.matcher(input.text());
                        if (matcher.matches()) {
                            Integer integer = Ints.tryParse(matcher.group(1));
                            if (integer != null) {
                                seasonNumber = integer;
                            }
                            return null;
                        }
                        Link releaseLink = new Link();
                        try {
                            releaseLink.episode = Ints.tryParse(input.select("td:eq(0) a").get(0).text());
                        } catch (Exception e) {
                            // eating this one
                        }
                        input = input.select("td:eq(1) a").first();
                        releaseLink.title = input.text();
                        releaseLink.link = input.attr("href");
                        releaseLink.season = seasonNumber;
                        releaseLink.groupTitle = seriesTitle;
                        return releaseLink;
                    }
                }), Predicates.and(Predicates.notNull(), NOT_SERIES_BUNDLE_LINK)));
    }
}
