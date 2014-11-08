package com.github.tarasmazepa.uateam.uateamtv.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.activity.BaseActivity;
import com.github.tarasmazepa.uateam.uateamtv.analytics.Analytics;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.fragment.base.BaseFragment;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseFragment extends BaseFragment {
    private static final String QUERY_POSTER = "div.article-content img";
    private static final String QUERY_DIV_ONLINE_CODE = "div#online_code param[name=flashvars]";
    private static final String ATTR_VALUE = "value";
    private static final String REGEXP_WATCH_ONLINE_FILE = ".*file\\=((http|https|ftp)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}/[a-zA-Z0-9\\-\\._/\\\\]+\\.mp4).*";

    private String posterLink;
    private String watchOnlineLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void loadResult() {
        new ResultTask<String, Void, String>() {
            @Override
            protected String produceData(String... strings) throws Throwable {
                Document document = Uateamtv.page(strings[0]);
                Elements elements = document.select(QUERY_DIV_ONLINE_CODE);
                if (elements.size() > 0) {
                    String stringWithFilePath = elements.first().attr(ATTR_VALUE);
                    if (stringWithFilePath != null) {
                        Pattern pattern = Pattern.compile(REGEXP_WATCH_ONLINE_FILE);
                        Matcher matcher = pattern.matcher(stringWithFilePath);
                        if (matcher.find()) {
                            watchOnlineLink = matcher.group(1);
                        }
                    }
                }
                elements = document.select(QUERY_POSTER);
                if (elements.size() > 0) {
                    posterLink = elements.first().attr("src");
                }
                return "";
            }

            @Override
            protected void onPostExecute(Result<String> result) {
                if (result.success) {
                    prepareView();
                } else {
                    onFinishLoading(false);
                }
            }
        }.execute(getActivity().getIntent().getStringExtra(BaseActivity.KEY_LINK));
    }

    @Override
    protected View createView(LayoutInflater inflater, SwipeRefreshLayout swipeRefreshLayout, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.release, swipeRefreshLayout, false);
    }

    @Override
    protected Analytics.ScreenName getScreenName() {
        return Analytics.ScreenName.RELEASE;
    }

    @Override
    protected String getUrl() {
        return getActivity().getIntent().getStringExtra(BaseActivity.KEY_LINK);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.release, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_watch).setVisible(watchOnlineLink != null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_watch:
                analytics.action(Analytics.Action.WATCH_VIDEO);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(watchOnlineLink)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareView() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.invalidateOptionsMenu();
        }
        Picasso.with(getActivity()).load(posterLink).into((android.widget.ImageView) getView().findViewById(R.id.poster), new Callback() {
            @Override
            public void onSuccess() {
                onFinishLoading(true);
                Palette.generateAsync(((BitmapDrawable) ((ImageView) getView().findViewById(R.id.poster)).getDrawable()).getBitmap(), new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch swatch = palette.getDarkMutedSwatch();
                        if (swatch == null) {
                            if (palette.getSwatches().isEmpty()) {
                                return;
                            } else {
                                swatch = palette.getSwatches().get(0);
                            }
                        }
                        swipeRefreshLayout.setBackgroundColor(swatch.getRgb());
                    }
                });
            }

            @Override
            public void onError() {
                onFinishLoading(false);
            }
        });
    }
}
