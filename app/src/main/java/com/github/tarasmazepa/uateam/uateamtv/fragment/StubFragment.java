package com.github.tarasmazepa.uateam.uateamtv.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.google.common.base.Joiner;

import java.util.List;

public class StubFragment extends BaseFragment {
    protected TextView textView;

    public static StubFragment create(int position) {
        return create(new StubFragment(), position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stub, container, false);
        textView = (TextView) view.findViewById(R.id.text);
        textView.setText("Loading?");
        return view;
    }

    public void onDataLoaded(List<String> strings) {
        textView.setText(Joiner.on('\n').join(strings));
    }
}
