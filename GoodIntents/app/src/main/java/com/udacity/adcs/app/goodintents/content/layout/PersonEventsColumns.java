package com.udacity.adcs.app.goodintents.content.layout;

import android.net.Uri;
import android.provider.BaseColumns;

import com.udacity.adcs.app.goodintents.content.AppContentProvider;

/**
 * The events available.
 */
public interface PersonEventsColumns extends BaseColumns {

    String TABLE_NAME = "person_events";
    Uri CONTENT_URI = Uri.parse(AppContentProvider.CONTENT_URI + TABLE_NAME);
    Uri CONTENT_URI_BY_TYPE_ID = Uri.parse(AppContentProvider.CONTENT_URI + TABLE_NAME + "/typeid");
    String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.bsu" + TABLE_NAME;
    String CONTENT_ITEMTYPE = "vnd.android.cursor.item/vnd.bsu" + TABLE_NAME;

    String _ID = "_id";
    String PERSON_ID = "person_id";
    String EVENT_ID = "event_id";
    String DATE = "participation_date";
    String POINTS = "points";

    String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + PERSON_ID + " INTEGER, "
                    + EVENT_ID + " INTEGER, "
                    + DATE + " INTEGER, "
                    + POINTS + " INTEGER "
                    + ");";

    String DEFAULT_SORT_ORDER = TABLE_NAME + "." + _ID + " DESC";
}
