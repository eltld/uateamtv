package com.github.tarasmazepa.uateam.uateamtv.fragment;

import com.github.tarasmazepa.uateam.uateamtv.fragment.base.AutoFragment;

public class MoviesFragment extends AutoFragment {
    public static MoviesFragment create(int position) {
        return create(new MoviesFragment(), "/movies", position);
    }
}
