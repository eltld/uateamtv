package com.github.tarasmazepa.uateam.uateamtv.fragment;

import com.github.tarasmazepa.uateam.uateamtv.fragment.base.ReleaseListFragment;

public class CartoonsFragment extends ReleaseListFragment {
    public static CartoonsFragment create(int position) {
        return create(new CartoonsFragment(), "/cartoons", position);
    }
}
