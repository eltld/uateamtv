package com.github.tarasmazepa.uateam.uateamtv.view;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FindView {
    protected final ViewSource viewSource;

    public FindView(ViewSource viewSource) {
        this.viewSource = viewSource;
    }

    public static FindView inView(final View view) {
        return new FindView(new ViewSource() {
            @Override
            public View get(int id) {
                return view.findViewById(id);
            }
        });
    }

    public static FindView inTag(final View view) {
        return new FindView(new ViewSource() {
            @Override
            public View get(int id) {
                return (View) view.getTag(id);
            }
        });
    }

    public static FindView inActivity(final Activity activity) {
        return new FindView(new ViewSource() {
            @Override
            public View get(int id) {
                return activity.findViewById(id);
            }
        });
    }

    public static View holdViewInTags(View root, int... ids) {
        for (int id : ids) {
            root.setTag(id, root.findViewById(id));
        }
        return root;
    }

    public <T extends View> T find(int id) {
        return (T) viewSource.get(id);
    }

    public TextView text(int id) {
        return find(id);
    }

    public EditText edit(int id) {
        return find(id);
    }

    public ImageView image(int id) {
        return find(id);
    }

    public Button button(int id) {
        return find(id);
    }

    public ImageButton imageButton(int id) {
        return find(id);
    }

    public CompoundButton compoundButton(int id) {
        return find(id);
    }

    public FindView visibility(int id, boolean visible) {
        find(id).setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public FindView visible(int id) {
        visibility(id, true);
        return this;
    }

    public FindView gone(int id) {
        visibility(id, false);
        return this;
    }

    public CharSequence asText(int id) {
        return text(id).getText();
    }

    public boolean asChecked(int id) {
        return compoundButton(id).isChecked();
    }

    interface ViewSource {
        View get(int id);
    }
}
