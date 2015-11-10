package com.udacity.adcs.app.goodintents.content.layout;

import android.net.Uri;
import android.provider.BaseColumns;

import com.udacity.adcs.app.goodintents.content.AppContentProvider;

/**
 * The events available.
 */
public interface EventsColumns extends BaseColumns {

    String TABLE_NAME = "event";
    Uri CONTENT_URI = Uri.parse(AppContentProvider.CONTENT_URI + TABLE_NAME);
    String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.bsu" + TABLE_NAME;
    String CONTENT_ITEMTYPE = "vnd.android.cursor.item/vnd.bsu" + TABLE_NAME;

    String _ID = "_id";
    String NAME = "name";
    String DESC = "desc";
    String ORGANIZATION = "organization";
    String DATE = "event_date";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";
    String DISPLAY_ADDRESS = "display_address";
    String PHOTO_URL = "event_photo_url";
    String ORG_PHOTO_URL = "org_photo_url";

    String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + NAME + " TEXT, "
                    + DESC + " TEXT, "
                    + ORGANIZATION + " TEXT, "
                    + DATE + " INTEGER, "
                    + LATITUDE + " INTEGER, "
                    + LONGITUDE + " INTEGER, "
                    + DISPLAY_ADDRESS + " TEXT, "
                    + PHOTO_URL + " TEXT, "
                    + ORG_PHOTO_URL + " TEXT "
                    + ");";

    String DEFAULT_SORT_ORDER = TABLE_NAME + "." + _ID + " DESC";
}
