package com.github.tarasmazepa.uateam.uateamtv.fragment.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.github.tarasmazepa.uateam.uateamtv.activity.MainActivity;
import com.github.tarasmazepa.uateam.uateamtv.util.Fragments;

public class BaseFragment extends Fragment {
    protected static final String KEY_SECTION_NUMBER = "section_number";

    public static <T extends Fragment> T create(T fragment, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SECTION_NUMBER, position);
        return Fragments.setArguments(fragment, bundle);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).onSectionAttached(getPosition());
        }
    }

    public int getPosition() {
        return getArguments().getInt(KEY_SECTION_NUMBER);
    }
}
