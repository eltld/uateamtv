package com.github.tarasmazepa.uateam.uateamtv.fragment;

import android.os.Bundle;
import android.view.View;

import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.List;

public class SeriesFragment extends StubFragment {
    public static SeriesFragment create(int position) {
        return create(new SeriesFragment(), position);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ResultTask<Void, Void, List<String>>() {
            @Override
            protected List<String> produceData(Void... voids) throws Throwable {
                return Lists.newArrayList(Collections2.transform(Jsoup.connect("http://uateam.tv/").timeout(12000).get().select("div#ja-col1 table").first().select("a"), new Function<Element, String>() {
                    @Override
                    public String apply(Element input) {
                        return input.text();
                    }
                }));
            }

            @Override
            protected void onPostExecute(Result<List<String>> result) {
                if (result.success) {
                    onDataLoaded(result.data);
                } else {
                    textView.setError(result.error.getMessage());
                }
            }
        }.execute();
    }
}
