package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import android.os.Bundle;

import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.model.Link;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.github.tarasmazepa.uateam.uateamtv.util.Fragments;

import java.util.List;

public class AutoFragment extends LinkListFragment {
    public static final String KEY_LINK = "link";

    public static AutoFragment create(String link) {
        return create(new AutoFragment(), link, 0);
    }

    public static <T extends AutoFragment> T create(T fragment, String link, int position) {
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
}
