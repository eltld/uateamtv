package com.github.tarasmazepa.uateam.uateamtv.fragment;

import android.view.View;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.adapter.BaseViewFiller;
import com.github.tarasmazepa.uateam.uateamtv.adapter.ListAdapter;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.model.Release;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.github.tarasmazepa.uateam.uateamtv.view.FindView;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class RecentReleasesFragment extends ListFragment<Release> {
    private static final String TAG = RecentReleasesFragment.class.getSimpleName();

    public static RecentReleasesFragment create(int position) {
        return create(new RecentReleasesFragment(), position);
    }

    @Override
    protected ListAdapter.ViewFiller<Release> getViewFiller() {
        return new BaseViewFiller<Release>() {
            @Override
            public void fillView(int position, View view, Release release) {
                FindView find = FindView.inTag(view);
                find.text(R.id.line1).setText(release.groupTitle);
                find.text(R.id.line2).setText(release.title);
                if (release.season != 0 || release.episode != 0) {
                    find.text(R.id.line3).setText(release.season + " сезон " + release.episode + " серія");
                }
                Picasso.with(getActivity()).load(release.imageLink).into(find.image(R.id.image));
            }
        };
    }

    @Override
    protected void loadResult() {
        new ResultTask<Void, Void, List<Release>>() {
            @Override
            protected List<Release> produceData(Void... params) throws Throwable {
                return Lists.newArrayList(Collections2.transform(
                        Uateamtv.home().select("div.freshrelease"), new Function<Element, Release>() {
                            @Override
                            public Release apply(Element element) {
                                Release release = new Release();
                                Elements elements = element.select("> p > span");
                                int elementsSize = elements.size();
                                if (elementsSize > 0) {
                                    release.groupTitle = elements.get(0).text();
                                    if (elementsSize > 1) {
                                        release.title = elements.get(1).text();
                                    }
                                }
                                elements = element.select("> div");
                                if (elements.size() > 0) {
                                    String releaseNumber = elements.first().text();
                                    if (releaseNumber != null && releaseNumber.length() > 0) {
                                        String[] numbers = releaseNumber.split("\\.");
                                        try {
                                            release.season = Integer.parseInt(numbers[0]);
                                            release.episode = Integer.parseInt(numbers[1]);
                                        } catch (NumberFormatException exception) {
                                        }
                                    }
                                }
                                elements = element.select("> p > a");
                                if (elements.size() > 0) {
                                    release.link = elements.first().attr("abs:href");
                                }
                                elements = element.select("> p > a > img");
                                if (elements.size() > 0) {
                                    release.imageLink = elements.first().attr("abs:src");
                                }
                                return release;
                            }
                        }
                ));
            }

            @Override
            protected void onPostExecute(Result<List<Release>> result) {
                onFinishLoading(result);
            }
        }.execute();
    }
}
