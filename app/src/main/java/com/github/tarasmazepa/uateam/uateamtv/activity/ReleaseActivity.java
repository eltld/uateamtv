package com.github.tarasmazepa.uateam.uateamtv.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.PaletteItem;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.github.tarasmazepa.uateam.uateamtv.view.FindView;
import com.google.common.base.Strings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseActivity extends BaseActivity {
    public static void start(Activity activity, String link, String title, String subtitle) {
        if (Strings.isNullOrEmpty(subtitle)) {
            subtitle = "";
        } else {
            subtitle = " - " + subtitle;
        }
        start(ReleaseActivity.class, activity, link, title + subtitle);
    }

    private static final String QUERY_POSTER = "div.article-content img";
    private static final String QUERY_DIV_ONLINE_CODE = "div#online_code param[name=flashvars]";
    private static final String ATTR_VALUE = "value";
    private static final String REGEXP_WATCH_ONLINE_FILE = ".*file\\=((http|https|ftp)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}/[a-zA-Z0-9\\-\\._/\\\\]+\\.mp4).*";

    private static final String KEY_POSTER_LINK = "poster_link";
    private static final String KEY_WATCH_ONLINE_LINK = "watch_online_link";

    private String posterLink;
    private String watchOnlineLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        if (savedInstanceState != null) {
            posterLink = savedInstanceState.getString(KEY_POSTER_LINK);
            watchOnlineLink = savedInstanceState.getString(KEY_WATCH_ONLINE_LINK);
            if (posterLink != null) {
                prepareView();
            }
        }
        if (posterLink == null) {
            new ResultTask<String, Void, Void>() {
                @Override
                protected Void produceData(String... strings) throws Throwable {
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
                    return null;
                }

                @Override
                protected void onPostExecute(Result<Void> result) {
                    prepareView();
                }
            }.execute(getIntent().getStringExtra(KEY_LINK));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_POSTER_LINK, posterLink);
        outState.putString(KEY_WATCH_ONLINE_LINK, watchOnlineLink);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.release, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_watch).setVisible(watchOnlineLink != null);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_watch:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(watchOnlineLink)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareView() {
        invalidateOptionsMenu();
        Picasso.with(ReleaseActivity.this).load(posterLink).into((android.widget.ImageView) findViewById(R.id.poster), new Callback() {
            @Override
            public void onSuccess() {
                Palette.generateAsync(((BitmapDrawable) ((ImageView) findViewById(R.id.poster)).getDrawable()).getBitmap(), new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        PaletteItem paletteItem = palette.getDarkVibrantColor();
                        if (paletteItem == null) {
                            if (palette.getPallete().isEmpty()) {
                                return;
                            } else {
                                paletteItem = palette.getPallete().get(0);
                            }
                        }
                        FindView.inActivity(ReleaseActivity.this).backgraundColor(R.id.container, paletteItem.getRgb());
                    }
                });
            }

            @Override
            public void onError() {
            }
        });
    }
}
