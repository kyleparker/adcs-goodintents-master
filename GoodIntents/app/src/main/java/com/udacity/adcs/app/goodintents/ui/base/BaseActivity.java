package com.udacity.adcs.app.goodintents.ui.base;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.ui.MainActivity;
import com.udacity.adcs.app.goodintents.utils.IntentUtils;

/**
 * Created by kyleparker on 11/9/2015.
 */
public class BaseActivity extends AppCompatActivity {
    protected static AppCompatActivity mActivity;

    // SearchView
    protected SearchView mSearchView;
    protected MenuItem mSearchItem;

    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;
    }

    /**
     * Callback when the home menu item is selected.
     */
    protected void onHomeSelected() {
        startActivity(IntentUtils.newIntent(mActivity, MainActivity.class));
        finish();
    }

    /**
     * Retrieve the base toolbar for the activity.
     *
     * @return
     */
    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }

        return mActionBarToolbar;
    }

    /**
     * Reset the search view to its original/unopened state
     */
    protected void resetSearchView() {
        if (mSearchView != null) {
            mSearchView.clearFocus();
            mSearchView.setIconified(true);
            mSearchView.setIconifiedByDefault(true);
        }

        if (mSearchItem != null) {
            mSearchItem.collapseActionView();
        }

        mActivity.invalidateOptionsMenu();
    }

    /**
     * Setup the search view for the activity
     *
     * @param menu
     */
    protected void setupSearchView(Menu menu) {
        mSearchItem = menu.findItem(R.id.menu_search);

        mSearchView = null;
        if (mSearchItem != null) {
            mSearchView = (SearchView) mSearchItem.getActionView();
        }
        if (mSearchView != null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchableInfo info = searchManager.getSearchableInfo(mActivity.getComponentName());
            mSearchView.setSearchableInfo(info);
            mSearchView.setIconified(true);
            mSearchView.setQueryRefinementEnabled(true);
        }
    }
}
