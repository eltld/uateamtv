package com.github.tarasmazepa.uateam.uateamtv.fragment;

import com.github.tarasmazepa.uateam.uateamtv.fragment.base.ReleaseListFragment;

public class MoviesFragment extends ReleaseListFragment {
    public static MoviesFragment create(int position) {
        return create(new MoviesFragment(), "/movies", position);
    }
}
