package com.udacity.adcs.app.goodintents.content;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.SearchRecentSuggestionsProvider;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.content.layout.SearchColumns;
import com.udacity.adcs.app.goodintents.objects.Search;
import com.udacity.adcs.app.goodintents.utils.Constants;

/**
 * Search recent suggestions provider for the searchview
 *
 * Created by kyleparker on 3/19/2015.
 */
public class SearchRecentProvider extends SearchRecentSuggestionsProvider {

    private UriMatcher matcher;

    public static final String AUTHORITY = "com.udacity.adcs.app.goodintents.SearchRecentProvider";
    public static final int MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES;

    private static final int SUGGESTIONS_CODE = 5;

    public SearchRecentProvider() {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SUGGESTIONS_CODE);
        setupSuggestions(AUTHORITY, MODE);
    }

    /**
     * Query the Traveler database and add our own suggestions to the results
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int code = matcher.match(uri);

        switch (code) {
            case SUGGESTIONS_CODE:
                if (selectionArgs == null || selectionArgs.length == 0 || selectionArgs[0].trim().length() == 0) {
                    return super.query(uri, projection, selection, selectionArgs, sortOrder);
                } else {
                    ContentResolver contentResolver = getContext().getContentResolver();
                    String[] columns = new String[] {
                            BaseColumns._ID,
                            SearchManager.SUGGEST_COLUMN_TEXT_1,
                            SearchManager.SUGGEST_COLUMN_QUERY,
                            SearchManager.SUGGEST_COLUMN_TEXT_2,
                            SearchManager.SUGGEST_COLUMN_ICON_1
                    };
                    MatrixCursor matrixCursor = new MatrixCursor(columns);

                    Cursor cursor = null;

                    try {
                        String searchSelection = SearchColumns.TABLE_NAME + " MATCH ?";
                        String[] searchSelectionArgs = new String[]{selectionArgs[0] + "*"};
                        String[] searchProjection = new String[]{
                                SearchColumns.ITEM_ID,
                                SearchColumns.NAME,
                                SearchColumns.TYPE_ID
                        };

                        cursor = contentResolver.query(SearchColumns.CONTENT_URI, searchProjection, searchSelection,
                                searchSelectionArgs, SearchColumns.DEFAULT_SORT_ORDER);

                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    int idxId = cursor.getColumnIndex(SearchColumns.ITEM_ID);
                                    int idxName = cursor.getColumnIndex(SearchColumns.NAME);
                                    int idxTypeId = cursor.getColumnIndex(SearchColumns.TYPE_ID);

                                    Search search = new Search();

                                    if (idxId > -1) {
                                        search.setId(cursor.getLong(idxId));
                                    }
                                    if (idxName > -1) {
                                        search.setName(cursor.getString(idxName));
                                    }
                                    if (idxTypeId > -1) {
                                        search.setTypeId(cursor.getLong(idxTypeId));
                                    }

                                    Uri iconUri = Uri.parse("android.resource://" + getContext().getPackageName() +
                                            "/drawable/ic_search_suggestion_photo");
                                    String itemType = getContext().getString(R.string.content_search_event);

                                    if (search.getTypeId() == Constants.Type.PERSON) {
                                        iconUri = Uri.parse("android.resource://" + getContext().getPackageName() +
                                                "/drawable/ic_search_person");
                                        itemType = getContext().getString(R.string.content_search_person);
                                    } else if (search.getTypeId() == Constants.Type.EVENT) {
                                        iconUri = Uri.parse("android.resource://" + getContext().getPackageName() +
                                                "/drawable/ic_search_event");
                                        itemType = getContext().getString(R.string.content_search_event);
                                    }

                                    matrixCursor.addRow(new Object[]{matrixCursor.getCount(), search.getName(),
                                            Constants.VALUE_SEARCH_SUGGESTION_PREFIX + search.getId() + Constants.VALUE_STRING_DELIMITER +
                                                    search.getTypeId(), itemType, iconUri});
                                } while (cursor.moveToNext());
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }

                    return matrixCursor;
                }
            default:
                return super.query(uri, projection, selection, selectionArgs, sortOrder);
        }
    }
}