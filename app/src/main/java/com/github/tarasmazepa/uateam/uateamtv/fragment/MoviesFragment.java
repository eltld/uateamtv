package com.github.tarasmazepa.uateam.uateamtv.fragment;

import android.os.Bundle;
import android.view.View;

import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoviesFragment extends StubFragment {
    public static MoviesFragment create(int position) {
        return create(new MoviesFragment(), position);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ResultTask<Void, Void, List<Link>>() {
            @Override
            protected List<Link> produceData(Void... voids) throws Throwable {
                return transformToLinks(Uateamtv.page("/movies").select(Uateamtv.SELECT));
            }

            @Override
            protected void onPostExecute(Result<List<Link>> listResult) {
                if(listResult.success) {
                    onDataLoaded(listResult.data);
                }
            }
        }.execute();
    }
}
