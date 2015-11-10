package com.udacity.adcs.app.goodintents.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.ui.base.BaseActivity;
import com.udacity.adcs.app.goodintents.ui.fragment.EventListFragment;
import com.udacity.adcs.app.goodintents.ui.fragment.FriendListFragment;
import com.udacity.adcs.app.goodintents.ui.fragment.ProfileFragment;
import com.udacity.adcs.app.goodintents.ui.view.ZoomOutSlideTransformer;
import com.udacity.adcs.app.goodintents.utils.PreferencesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 *
 * Created by kyleparker on 11/9/2015.
 */
public class FeedActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        setupViewPager();
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search, menu);

//        setupSearchView(menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_backup:
                handleBackup();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleBackup() {
        Runnable load = new Runnable() {
            public void run() {
                try {
                    File sd = new File(mActivity.getExternalFilesDir(null) + "/");
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {
                        String backupName = Long.toString(System.currentTimeMillis());

                        String currentDBPath = "//data//com.udacity.adcs.app.goodintents//databases//goodintents.db";
                        String backupDBPath = backupName + ".db";

                        File currentDB = new File(data, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        if (currentDB.exists()) {
                            FileInputStream is = new FileInputStream(currentDB);
                            FileOutputStream os = new FileOutputStream(backupDB);

                            FileChannel src = is.getChannel();
                            FileChannel dst = os.getChannel();
                            dst.transferFrom(src, 0, src.size());

                            is.close();
                            os.close();
                            src.close();
                            dst.close();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mActivity.runOnUiThread(backupRunnable);
                }
            }
        };

        Thread thread = new Thread(null, load, "handleBackup");
        thread.start();
    }

    private final Runnable backupRunnable = new Runnable() {
        public void run() {
            Toast.makeText(mActivity, "done", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Setup the toolbar for the activity
     */
    private void setupToolbar() {
        final Toolbar toolbar = getActionBarToolbar();
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle(mActivity.getString(R.string.app_name));
            }
        });
    }

    /**
     * Setup the viewpager for current and past trip lists
     */
    private void setupViewPager() {
        FragmentManager fm = getSupportFragmentManager();

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        if (viewPager != null) {
            int tabPosition = PreferencesUtils.getInt(mActivity, R.string.feed_list_selected_key, PreferencesUtils.Tab.FEED_LIST_EVENT);

            FeedPagerAdapter adapter = new FeedPagerAdapter(fm);
            viewPager.setAdapter(adapter);
            viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
            viewPager.setCurrentItem(tabPosition, true);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setSmoothScrollingEnabled(true);
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    PreferencesUtils.setInt(mActivity, R.string.feed_list_selected_key, tab.getPosition());
                    viewPager.setCurrentItem(tab.getPosition(), true);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    PreferencesUtils.setInt(mActivity, R.string.feed_list_selected_key, tab.getPosition());
                }
            });
            TabLayout.Tab tab = tabLayout.getTabAt(tabPosition);
            if (tab != null) {
                tab.select();
            }
        }
    }

    private class FeedPagerAdapter extends FragmentStatePagerAdapter {
        public FeedPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case PreferencesUtils.Tab.FEED_LIST_EVENT:
                    return mActivity.getString(R.string.title_events);
                case PreferencesUtils.Tab.FEED_LIST_FRIENDS:
                    return mActivity.getString(R.string.title_friends);
                case PreferencesUtils.Tab.FEED_LIST_PROFILE:
                    return mActivity.getString(R.string.title_profile);
                default:
                    return "";
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case PreferencesUtils.Tab.FEED_LIST_EVENT:
                    return EventListFragment.newInstance();
                case PreferencesUtils.Tab.FEED_LIST_FRIENDS:
                    return FriendListFragment.newInstance();
                case PreferencesUtils.Tab.FEED_LIST_PROFILE:
                    return ProfileFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
