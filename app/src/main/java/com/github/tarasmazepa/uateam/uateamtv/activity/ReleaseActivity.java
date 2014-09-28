package com.github.tarasmazepa.uateam.uateamtv.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.base.Result;
import com.github.tarasmazepa.uateam.uateamtv.model.Release;
import com.github.tarasmazepa.uateam.uateamtv.server.Uateamtv;
import com.github.tarasmazepa.uateam.uateamtv.task.ResultTask;
import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseActivity extends BaseActivity {
    public static void start(Activity activity, String link) {
        start(ReleaseActivity.class, activity, link);
    }

    private static final String QUERY_POSTER = "div.article-content img";
    private static final String QUERY_DIV_ONLINE_CODE = "div#online_code param[name=flashvars]";
    private static final String ATTR_VALUE = "value";
    private static final String REGEXP_WATCH_ONLINE_FILE = ".*file\\=((http|https|ftp)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}/[a-zA-Z0-9\\-\\._/\\\\]+\\.mp4).*";

    private String posterLink;
    private String watchOnlineLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
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
                protected void onPostExecute(Result<Void> voidResult) {
                    Picasso.with(ReleaseActivity.this).load(posterLink).into((android.widget.ImageView) findViewById(R.id.poster));
                    invalidateOptionsMenu();
                }
            }.execute(getIntent().getStringExtra(KEY_LINK));
        }
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
}
