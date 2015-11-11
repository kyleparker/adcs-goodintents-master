package com.udacity.adcs.app.goodintents.ui;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.content.SearchRecentProvider;
import com.udacity.adcs.app.goodintents.objects.Search;
import com.udacity.adcs.app.goodintents.ui.base.BaseActivity;
import com.udacity.adcs.app.goodintents.ui.list.SearchResultAdapter;
import com.udacity.adcs.app.goodintents.utils.Constants;
import com.udacity.adcs.app.goodintents.utils.DialogUtils;
import com.udacity.adcs.app.goodintents.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by kyleparker on 11/9/2015.
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private MaterialDialog mProgressDialog;

    private SearchResultAdapter mAdapter;

    private List<Search> mSearchResults;
    private Search mSearch;

    private String mQuery;
    private boolean mFinishActivity = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.search_result_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        onNewIntent(getIntent());
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mActivity.getMenuInflater().inflate(R.menu.search, menu);

        mSearchItem = menu.findItem(R.id.menu_search);

        if (mSearchItem != null) {
            mSearchView = (SearchView) mSearchItem.getActionView();
        }
        if (mSearchView != null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchableInfo info = searchManager.getSearchableInfo(mActivity.getComponentName());
            mSearchView.setSearchableInfo(info);
            mSearchView.setIconified(true);
            mSearchView.setIconifiedByDefault(true);
            mSearchView.setQueryRefinementEnabled(true);
            mSearchView.setOnQueryTextListener(this);
        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Load the search results based on the incoming query and set up the gridview and adapter
     */
    private void getSearchResults() {
        try {
            mProgressDialog = DialogUtils.createSpinnerProgressDialog(mActivity, DialogUtils.DEFAULT_TITLE_ID, R.string.dialog_loading, false);
            mProgressDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mSearchResults = new ArrayList<>();
        mAdapter = new SearchResultAdapter(mActivity, mActivity.getResources().getInteger(R.integer.activities_per_row));
        mAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mSearch = mAdapter.getItem(position);
                handleSearchResult();
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        Runnable load = new Runnable() {
            public void run() {
                try {
                    if (mSearchResults == null || mSearchResults.isEmpty()) {
                        mSearchResults = new ArrayList<>();
                        mSearchResults = mProvider.getSearchList(mQuery + "*");
                    }

                    if (mSearchResults != null && mSearchResults.size() > 0) {
                        for (int i = 0; i < mSearchResults.size(); i++) {
                            if (mSearchResults.get(i).getTypeId() == Constants.Type.EVENT) {
                                mSearchResults.get(i).event = mProvider.getEvent(mSearchResults.get(i).getId());
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mActivity.runOnUiThread(getSearchResultsRunnable);
                }
            }
        };

        Thread thread = new Thread(null, load, "getSearchResults");
        thread.start();
    }

    private final Runnable getSearchResultsRunnable = new Runnable() {
        public void run() {
            if (mSearchResults != null && mSearchResults.size() > 0) {
                mAdapter.addAll(mSearchResults);
                findViewById(R.id.empty_search_result_container).setVisibility(View.GONE);
            } else {
                showEmptyView();
            }

            try {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    /**
     *
     * @param itemId
     * @param typeId
     */
    private void getSearchSuggestions(final long itemId, final long typeId) {
        try {
            mProgressDialog = DialogUtils.createSpinnerProgressDialog(mActivity, DialogUtils.DEFAULT_TITLE_ID, R.string.dialog_loading, false);
            mProgressDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Runnable load = new Runnable() {
            public void run() {
                try {
                    if (mSearch == null) {
                        mSearch = mProvider.getSearch(itemId, typeId);
                    }

                    if (typeId == Constants.Type.PERSON) {
                    } else if (typeId == Constants.Type.EVENT) {
                        Intent intent = IntentUtils.newIntent(mActivity, EventDetailActivity.class);
                        intent.putExtra(Constants.Extra.EVENT_ID, mSearch.getId());
                        mActivity.startActivity(intent);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mActivity.runOnUiThread(getSearchSuggestionsRunnable);
                }
            }
        };

        Thread thread = new Thread(null, load, "getSearchSuggestions");
        thread.start();
    }

    /**
     * Once the search suggestion data has been loaded, handle the search result and direct the user to the proper
     * activity.
     */
    private final Runnable getSearchSuggestionsRunnable = new Runnable() {
        public void run() {
            handleSearchResult();

            try {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    /**
     * Handle the incoming search intent for the activity. Set the toolbar to display the query, save the query to
     * the search suggestions and split the query from the search suggestion.
     * @param intent
     */
    private void handleIntent(Intent intent) {
        setIntent(intent);
        mQuery = intent.getStringExtra(SearchManager.QUERY);

        mToolbar.post(new Runnable() {
            @Override
            public void run() {
                mToolbar.setTitle(TextUtils.isEmpty(mQuery) ? mActivity.getString(R.string.app_name) : mQuery);
            }
        });

        if (mSearchView != null) {
            resetSearchView();
        }

        // Add the query to the recent search provider
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(mActivity, SearchRecentProvider.AUTHORITY,
                SearchRecentProvider.MODE);
        suggestions.saveRecentQuery(mQuery, null);

        if (mQuery.contains(Constants.VALUE_SEARCH_SUGGESTION_PREFIX)) {
            String[] parts = mQuery.replace(Constants.VALUE_SEARCH_SUGGESTION_PREFIX, "").split(Pattern.quote("|"));
            if (parts.length > 0) {
                long itemId = Long.parseLong(parts[0]);
                long typeId = Long.parseLong(parts[1]);

                mFinishActivity = true;
                getSearchSuggestions(itemId, typeId);
            }
        } else {
            getSearchResults();
        }
    }

    /**
     * Handle the search result and direct the user to the proper activity based on the typeId
     */
    private void handleSearchResult() {
        if (mSearch ==  null) {
            showEmptyView();
            return;
        }

        Intent intent;

        if (mSearch.getTypeId() == Constants.Type.PERSON) {
        } else if (mSearch.getTypeId() == Constants.Type.EVENT) {
            intent = IntentUtils.newIntent(mActivity, EventDetailActivity.class);
            intent.putExtra(Constants.Extra.EVENT_ID, mSearch.getId());
            mActivity.startActivity(intent);
        }

        if (mFinishActivity) {
            mActivity.finish();
        }
    }

    private void setupToolbar() {
        mToolbar = getActionBarToolbar();
        mToolbar.setNavigationIcon(R.drawable.ic_action_up);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
    }

    /**
     * If there were no results, show an empty view with a message to the user
     */
    private void showEmptyView() {
        findViewById(R.id.empty_search_result_container).setVisibility(View.VISIBLE);
        TextView empty = (TextView) findViewById(R.id.empty_search_result_list_message);
        empty.setText(mActivity.getString(R.string.content_empty_search_result, mQuery));

        resetSearchView();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        resetSearchView();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        mSearchResults = new ArrayList<>();
        mSearch = null;

        return false;
    }
}
