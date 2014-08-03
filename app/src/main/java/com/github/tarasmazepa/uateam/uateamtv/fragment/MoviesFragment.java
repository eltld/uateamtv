package com.github.tarasmazepa.uateam.uateamtv.fragment;

public class MoviesFragment extends AutoFragment {
    public static MoviesFragment create(int position) {
        return create(new MoviesFragment(), "/movies", position);
    }
}
