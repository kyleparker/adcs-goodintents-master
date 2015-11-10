package com.udacity.adcs.app.goodintents.content.layout;

import android.net.Uri;
import android.provider.BaseColumns;

import com.udacity.adcs.app.goodintents.content.AppContentProvider;

/**
 * The events available.
 */
public interface PersonMediaColumns extends BaseColumns {

    String TABLE_NAME = "person_media";
    Uri CONTENT_URI = Uri.parse(AppContentProvider.CONTENT_URI + TABLE_NAME);
    Uri CONTENT_URI_BY_PERSON_EVENT = Uri.parse(AppContentProvider.CONTENT_URI + TABLE_NAME + "/personevent");
    String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.bsu" + TABLE_NAME;
    String CONTENT_ITEMTYPE = "vnd.android.cursor.item/vnd.bsu" + TABLE_NAME;

    String _ID = "_id";
    String PERSON_EVENTS_ID = "person_events_id";
    String LOCAL_STORAGE_URL = "local_storage_url";
    String MEDIA_NAME = "media_name";

    public String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + PERSON_EVENTS_ID + " INTEGER, "
                    + LOCAL_STORAGE_URL + " TEXT, "
                    + MEDIA_NAME + " TEXT "
                    + ");";

    public String DEFAULT_SORT_ORDER = TABLE_NAME + "." + _ID + " DESC";
}
