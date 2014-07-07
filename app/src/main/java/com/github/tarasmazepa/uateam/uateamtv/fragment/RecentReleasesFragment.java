package com.github.tarasmazepa.uateam.uateamtv.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.activity.MainActivity;
import com.github.tarasmazepa.uateam.uateamtv.adapter.BaseListAdapter;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.model.Release;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.github.tarasmazepa.uateam.uateamtv.view.FindView;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class RecentReleasesFragment extends ListFragment {
    private static final String TAG = RecentReleasesFragment.class.getSimpleName();

    private static final String ARG_SECTION_NUMBER = "section_number";

    public RecentReleasesFragment() {
        super();
        setRetainInstance(true);
    }

    public static RecentReleasesFragment newInstance(int sectionNumber) {
        RecentReleasesFragment fragment = new RecentReleasesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getListAdapter() == null) {
            new ResultTask<Void, Void, List<Release>>() {
                @Override
                protected List<Release> produceData(Void... params) throws Throwable {
                    return Lists.newArrayList(Collections2.transform(
                            Jsoup.connect("http://uateam.tv/").timeout(12000).get().select("div.freshrelease"), new Function<Element, Release>() {
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
                                        release.detailsLink = elements.first().attr("abs:href");
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
                    if (result.success) {
                        setListAdapter(new BaseListAdapter<Release>(getActivity(), result.data) {
                            @Override
                            protected void fillView(int position, View view, Release release) {
                                FindView find = FindView.inTag(view);
                                find.text(R.id.text1).setText(release.groupTitle);
                                find.text(R.id.text2).setText(release.title);
                                if (release.season != 0 || release.episode != 0) {
                                    find.text(R.id.text3).setText(release.season + " сезон " + release.episode + " серія");
                                }
                                Picasso.with(getContext()).load(release.imageLink).into(find.image(R.id.image));
                            }

                            @Override
                            protected View inflateView(int position, ViewGroup parent) {
                                return FindView.holdViewInTags(layoutInflater.inflate(R.layout.release_list_item, parent, false), R.id.text1, R.id.text2, R.id.text3, R.id.image);
                            }
                        });
                    } else {
                        Log.d(TAG, result.toString());
                    }
                }
            }.execute();
        }
    }

}
