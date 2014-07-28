package com.github.tarasmazepa.uateam.uateamtv.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.github.tarasmazepa.uateam.uateamtv.activity.MainActivity;

public class BaseFragment extends Fragment {
    protected static final String ARG_SECTION_NUMBER = "section_number";

    public static <T extends Fragment> T create(T fragment, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
