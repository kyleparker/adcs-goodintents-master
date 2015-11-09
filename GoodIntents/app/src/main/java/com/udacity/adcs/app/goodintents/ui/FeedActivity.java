package com.udacity.adcs.app.goodintents.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.ui.base.BaseActivity;
import com.udacity.adcs.app.goodintents.ui.fragment.ActivityListFragment;
import com.udacity.adcs.app.goodintents.ui.fragment.FriendListFragment;
import com.udacity.adcs.app.goodintents.ui.fragment.ProfileFragment;
import com.udacity.adcs.app.goodintents.ui.view.ZoomOutSlideTransformer;
import com.udacity.adcs.app.goodintents.utils.PreferencesUtils;

/**
 *
 * Created by kyleparker on 11/9/2015.
 */
public class FeedActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        setupViewPager();
    }

    /**
     * Setup the viewpager for current and past trip lists
     */
    private void setupViewPager() {
        FragmentManager fm = getSupportFragmentManager();

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        if (viewPager != null) {
            int tabPosition = PreferencesUtils.getInt(mActivity, R.string.feed_list_selected_key, PreferencesUtils.Tab.FEED_LIST_UPCOMING);

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
                case PreferencesUtils.Tab.FEED_LIST_UPCOMING:
                    return mActivity.getString(R.string.title_upcoming);
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
                case PreferencesUtils.Tab.FEED_LIST_UPCOMING:
                    return ActivityListFragment.newInstance();
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
