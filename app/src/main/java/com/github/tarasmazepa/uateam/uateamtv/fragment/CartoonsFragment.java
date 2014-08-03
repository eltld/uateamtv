package com.github.tarasmazepa.uateam.uateamtv.fragment;

public class CartoonsFragment extends AutoFragment {
    public static CartoonsFragment create(int position) {
        return create(new CartoonsFragment(), "/cartoons", position);
    }
}
