package com.github.tarasmazepa.uateam.uateamtv.util;

import android.app.Fragment;
import android.os.Bundle;

public class Fragments {
    public static <T extends Fragment> T setArguments(T fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        return fragment;
    }
}
