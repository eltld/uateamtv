package com.github.tarasmazepa.uateam.uateamtv.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.fragment.CartoonsFragment;
import com.github.tarasmazepa.uateam.uateamtv.fragment.MoviesFragment;
import com.github.tarasmazepa.uateam.uateamtv.fragment.NavigationDrawerFragment;
import com.github.tarasmazepa.uateam.uateamtv.fragment.RecentReleasesFragment;
import com.github.tarasmazepa.uateam.uateamtv.fragment.SeriesFragment;
import com.github.tarasmazepa.uateam.uateamtv.fragment.base.BaseFragment;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        BaseFragment fragment = (BaseFragment) getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || fragment.getPosition() != position) {
            switch (position) {
                case 0:
                    fragment = RecentReleasesFragment.create(position);
                    break;
                case 1:
                    fragment = SeriesFragment.create(position);
                    break;
                case 2:
                    fragment = MoviesFragment.create(position);
                    break;
                case 3:
                    fragment = CartoonsFragment.create(position);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section0);
                break;
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
}
