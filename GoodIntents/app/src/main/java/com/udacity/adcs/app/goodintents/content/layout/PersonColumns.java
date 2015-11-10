package com.udacity.adcs.app.goodintents.content.layout;

import android.net.Uri;
import android.provider.BaseColumns;

import com.udacity.adcs.app.goodintents.content.AppContentProvider;

/**
 * The events available.
 */
public interface PersonColumns extends BaseColumns {

    String TABLE_NAME = "person";
    Uri CONTENT_URI = Uri.parse(AppContentProvider.CONTENT_URI + TABLE_NAME);
    Uri CONTENT_URI_BY_GOOGLE_ACCOUNT = Uri.parse(AppContentProvider.CONTENT_URI + TABLE_NAME + "/googleaccount");
    String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.bsu" + TABLE_NAME;
    String CONTENT_ITEMTYPE = "vnd.android.cursor.item/vnd.bsu" + TABLE_NAME;

    String _ID = "_id";
    String DISPLAY_NAME = "display_name";
    String EMAIL_ADDRESS = "email_address";
    String GOOGLE_ACCOUNT_ID = "google_account_id";
    String PHOTO_URL = "photo_url";
    String TYPE_ID = "type_id";

    String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DISPLAY_NAME + " TEXT, "
                    + EMAIL_ADDRESS + " TEXT, "
                    + GOOGLE_ACCOUNT_ID + " TEXT, "
                    + PHOTO_URL + " TEXT, "
                    + TYPE_ID + " TEXT"
                    + ");";

    String DEFAULT_SORT_ORDER = TABLE_NAME + "." + _ID + " DESC";
}
