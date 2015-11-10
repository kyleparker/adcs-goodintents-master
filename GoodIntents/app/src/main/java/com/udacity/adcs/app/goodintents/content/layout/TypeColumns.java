package com.udacity.adcs.app.goodintents.content.layout;

import android.net.Uri;
import android.provider.BaseColumns;

import com.udacity.adcs.app.goodintents.content.AppContentProvider;

/**
 * The events available.
 */
public interface TypeColumns extends BaseColumns {

    String TABLE_NAME = "type";
    Uri CONTENT_URI = Uri.parse(AppContentProvider.CONTENT_URI + TABLE_NAME);
    String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.bsu" + TABLE_NAME;
    String CONTENT_ITEMTYPE = "vnd.android.cursor.item/vnd.bsu" + TABLE_NAME;

    String _ID = "_id";
    String TYPE_NAME = "type_name";

    String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TYPE_NAME + " TEXT"
                    + ");";

    String DEFAULT_SORT_ORDER = TABLE_NAME + "." + _ID + " DESC";
}
