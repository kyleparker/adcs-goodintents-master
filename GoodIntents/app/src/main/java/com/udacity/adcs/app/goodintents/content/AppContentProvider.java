package com.udacity.adcs.app.goodintents.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.udacity.adcs.app.goodintents.content.layout.EventsColumns;
import com.udacity.adcs.app.goodintents.content.layout.PersonColumns;
import com.udacity.adcs.app.goodintents.content.layout.PersonEventsColumns;
import com.udacity.adcs.app.goodintents.content.layout.PersonMediaColumns;
import com.udacity.adcs.app.goodintents.content.layout.TypeColumns;

/**
 * Content provider
 *
 * Created by kyleparker on 11/5/2015.
 */
public class AppContentProvider extends ContentProvider {
    private static final String TAG = AppContentProvider.class.getSimpleName();

    public static final String AUTHORITY = "com.udacity.adcs.app.goodintents";
    public static final String CONTENT_URI = "content://com.udacity.adcs.app.goodintents/";

    private static final String DATABASE_NAME = "goodintents.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_INNER_JOIN = " INNER JOIN ";
    private static final String SQL_OUTER_JOIN = " LEFT OUTER JOIN ";
    private static final String SQL_ON = " ON ";
    private static final String SQL_AND = " AND ";

    private static Context mContext;
    private static SQLiteDatabase mDb;
    private final UriMatcher mUriMatcher;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(EventsColumns.CREATE_TABLE);
            db.execSQL(PersonColumns.CREATE_TABLE);
            db.execSQL(PersonEventsColumns.CREATE_TABLE);
            db.execSQL(PersonMediaColumns.CREATE_TABLE);
            db.execSQL(TypeColumns.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private enum UrlType {
        EVENT, EVENT_ID,
        PERSON, PERSON_ID,
        PERSON_EVENTS, PERSON_EVENTS_ID, PERSON_EVENTS_TYPE_ID,
        PERSON_MEDIA, PERSON_MEDIA_ID, PERSON_MEDIA_PERSON_EVENT_ID
    }

    public AppContentProvider() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        mUriMatcher.addURI(AUTHORITY, EventsColumns.TABLE_NAME, UrlType.EVENT.ordinal());
        mUriMatcher.addURI(AUTHORITY, EventsColumns.TABLE_NAME + "/#", UrlType.EVENT_ID.ordinal());

        mUriMatcher.addURI(AUTHORITY, PersonColumns.TABLE_NAME, UrlType.PERSON.ordinal());
        mUriMatcher.addURI(AUTHORITY, PersonColumns.TABLE_NAME + "/#", UrlType.PERSON_ID.ordinal());

        mUriMatcher.addURI(AUTHORITY, PersonEventsColumns.TABLE_NAME, UrlType.PERSON_EVENTS.ordinal());
        mUriMatcher.addURI(AUTHORITY, PersonEventsColumns.TABLE_NAME + "/#", UrlType.PERSON_EVENTS_ID.ordinal());
        mUriMatcher.addURI(AUTHORITY, PersonEventsColumns.TABLE_NAME + "/typeid/#", UrlType.PERSON_EVENTS_TYPE_ID.ordinal());

        mUriMatcher.addURI(AUTHORITY, PersonMediaColumns.TABLE_NAME, UrlType.PERSON_MEDIA.ordinal());
        mUriMatcher.addURI(AUTHORITY, PersonMediaColumns.TABLE_NAME + "/#", UrlType.PERSON_MEDIA_ID.ordinal());
        mUriMatcher.addURI(AUTHORITY, PersonMediaColumns.TABLE_NAME + "/personevent/#", UrlType.PERSON_MEDIA_PERSON_EVENT_ID.ordinal());
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        try {
            mDb = databaseHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to open database for writing", e);
        }
        return mDb != null;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String sortOrder = null;

        switch (getUrlType(uri)) {
            case EVENT:
                queryBuilder.setTables(EventsColumns.TABLE_NAME);
                sortOrder = sort != null ? sort : EventsColumns.DEFAULT_SORT_ORDER;
                break;
            case EVENT_ID:
                queryBuilder.setTables(EventsColumns.TABLE_NAME);
                queryBuilder.appendWhere(EventsColumns._ID + " = " + uri.getPathSegments().get(1));
                break;
            case PERSON:
                queryBuilder.setTables(PersonColumns.TABLE_NAME);
                sortOrder = sort != null ? sort : PersonColumns.DEFAULT_SORT_ORDER;
                break;
            case PERSON_ID:
                queryBuilder.setTables(PersonColumns.TABLE_NAME);
                queryBuilder.appendWhere(PersonColumns._ID + " = " + uri.getPathSegments().get(1));
                break;
            case PERSON_EVENTS:
                queryBuilder.setTables(PersonEventsColumns.TABLE_NAME);
                sortOrder = sort != null ? sort : PersonEventsColumns.DEFAULT_SORT_ORDER;
                break;
            case PERSON_EVENTS_ID:
                queryBuilder.setTables(PersonEventsColumns.TABLE_NAME);
                queryBuilder.appendWhere(PersonEventsColumns._ID + " = " + uri.getPathSegments().get(1));
                break;
            case PERSON_EVENTS_TYPE_ID:
                queryBuilder.setTables(PersonEventsColumns.TABLE_NAME +
                        SQL_INNER_JOIN +
                        PersonColumns.TABLE_NAME + SQL_ON +
                        appendTableName(PersonColumns.TABLE_NAME, PersonColumns._ID) + " = " +
                        appendTableName(PersonEventsColumns.TABLE_NAME, PersonEventsColumns.PERSON_ID) +
                        SQL_INNER_JOIN +
                        EventsColumns.TABLE_NAME + SQL_ON +
                        appendTableName(EventsColumns.TABLE_NAME, EventsColumns._ID) + " = " +
                        appendTableName(PersonEventsColumns.TABLE_NAME, PersonEventsColumns.EVENT_ID));
                sortOrder = sort != null ? sort : PersonEventsColumns.DEFAULT_SORT_ORDER;
                break;
            case PERSON_MEDIA:
                queryBuilder.setTables(PersonMediaColumns.TABLE_NAME);
                sortOrder = sort != null ? sort : PersonMediaColumns.DEFAULT_SORT_ORDER;
                break;
            case PERSON_MEDIA_ID:
                queryBuilder.setTables(PersonMediaColumns.TABLE_NAME);
                queryBuilder.appendWhere(PersonMediaColumns._ID + " = " + uri.getPathSegments().get(1));
                break;
            case PERSON_MEDIA_PERSON_EVENT_ID:
                queryBuilder.setTables(PersonMediaColumns.TABLE_NAME +
                        SQL_INNER_JOIN +
                        PersonEventsColumns.TABLE_NAME + SQL_ON +
                        appendTableName(PersonEventsColumns.TABLE_NAME, PersonEventsColumns._ID) + " = " +
                        appendTableName(PersonMediaColumns.TABLE_NAME, PersonMediaColumns.PERSON_EVENTS_ID) +
                        SQL_INNER_JOIN +
                        PersonColumns.TABLE_NAME + SQL_ON +
                        appendTableName(PersonColumns.TABLE_NAME, PersonColumns._ID) + " = " +
                        appendTableName(PersonEventsColumns.TABLE_NAME, PersonEventsColumns.PERSON_ID) +
                        SQL_INNER_JOIN +
                        EventsColumns.TABLE_NAME + SQL_ON +
                        appendTableName(EventsColumns.TABLE_NAME, EventsColumns._ID) + " = " +
                        appendTableName(PersonEventsColumns.TABLE_NAME, PersonEventsColumns.EVENT_ID));
                sortOrder = sort != null ? sort : PersonEventsColumns.DEFAULT_SORT_ORDER;
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }

        Cursor cursor = queryBuilder.query(mDb, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(mContext.getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (getUrlType(uri)) {
            case EVENT:
                return EventsColumns.CONTENT_TYPE;
            case EVENT_ID:
                return EventsColumns.CONTENT_ITEMTYPE;
            case PERSON:
                return PersonColumns.CONTENT_TYPE;
            case PERSON_ID:
                return PersonColumns.CONTENT_ITEMTYPE;
            case PERSON_EVENTS:
                return EventsColumns.CONTENT_TYPE;
            case PERSON_EVENTS_ID:
                return EventsColumns.CONTENT_ITEMTYPE;
            case PERSON_MEDIA:
                return PersonMediaColumns.CONTENT_TYPE;
            case PERSON_MEDIA_ID:
                return PersonMediaColumns.CONTENT_ITEMTYPE;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        if (contentValues == null) {
            contentValues = new ContentValues();
        }
        Uri result = null;
        try {
            mDb.beginTransaction();
            result = insertContentValues(uri, getUrlType(uri), contentValues);
            mDb.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
            ex.printStackTrace();
        } finally {
            mDb.endTransaction();
        }
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, String where, String[] selectionArgs) {
        String table;

        switch (getUrlType(uri)) {
            case EVENT:
                table = EventsColumns.TABLE_NAME;
                break;
            case PERSON:
                table = PersonColumns.TABLE_NAME;
                break;
            case PERSON_EVENTS:
                table = PersonEventsColumns.TABLE_NAME;
                break;
            case PERSON_MEDIA:
                table = PersonMediaColumns.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }

        Log.w(TAG, "Deleting table " + table);
        int count = 0;
        try {
            mDb.beginTransaction();
            count = mDb.delete(table, where, selectionArgs);
            mDb.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
            ex.printStackTrace();
        } finally {
            mDb.endTransaction();
        }

        mContext.getContentResolver().notifyChange(uri, null, true);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String where, String[] selectionArgs) {
        String table;
        String whereClause;
        switch (getUrlType(uri)) {
            case EVENT:
                table = EventsColumns.TABLE_NAME;
                whereClause = where;
                break;
            case EVENT_ID:
                table = EventsColumns.TABLE_NAME;
                whereClause = EventsColumns._ID + "=" + uri.getPathSegments().get(1);
                if (!TextUtils.isEmpty(where)) {
                    whereClause += " AND (" + where + ")";
                }
                break;
            case PERSON:
                table = PersonColumns.TABLE_NAME;
                whereClause = where;
                break;
            case PERSON_ID:
                table = PersonColumns.TABLE_NAME;
                whereClause = PersonColumns._ID + "=" + uri.getPathSegments().get(1);
                if (!TextUtils.isEmpty(where)) {
                    whereClause += " AND (" + where + ")";
                }
                break;
            case PERSON_EVENTS:
                table = PersonEventsColumns.TABLE_NAME;
                whereClause = where;
                break;
            case PERSON_EVENTS_ID:
                table = PersonEventsColumns.TABLE_NAME;
                whereClause = PersonEventsColumns._ID + "=" + uri.getPathSegments().get(1);
                if (!TextUtils.isEmpty(where)) {
                    whereClause += " AND (" + where + ")";
                }
                break;
            case PERSON_MEDIA:
                table = PersonMediaColumns.TABLE_NAME;
                whereClause = where;
                break;
            case PERSON_MEDIA_ID:
                table = PersonMediaColumns.TABLE_NAME;
                whereClause = PersonMediaColumns._ID + "=" + uri.getPathSegments().get(1);
                if (!TextUtils.isEmpty(where)) {
                    whereClause += " AND (" + where + ")";
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }

        int count = 0;
        try {
            mDb.beginTransaction();
            count = mDb.update(table, values, whereClause, selectionArgs);
            mDb.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mDb.endTransaction();
        }
        mContext.getContentResolver().notifyChange(uri, null, true);
        return count;
    }

    private static String appendTableName(String table, String column) {
        return table + "." + column;
    }

    /**
     * Inserts a content based on the url type.
     *
     * @param uri the content uri
     * @param urlType the url type
     * @param contentValues the content values
     */
    private Uri insertContentValues(Uri uri, UrlType urlType, ContentValues contentValues) {
        switch (urlType) {
            case EVENT:
                return insertEvent(uri, contentValues);
            case PERSON:
                return insertPerson(uri, contentValues);
            case PERSON_EVENTS:
                return insertPersonEvent(uri, contentValues);
            case PERSON_MEDIA:
                return insertPersonMedia(uri, contentValues);
            default:
                throw new IllegalArgumentException("Unknown url " + uri);
        }
    }

    /**
     * Inserts the item
     *
     * @param uri the content uri
     * @param contentValues the content values
     */
    private Uri insertEvent(Uri uri, ContentValues contentValues) {
        long rowId = mDb.insert(EventsColumns.TABLE_NAME, EventsColumns._ID, contentValues);

        if (rowId >= 0) {
            uri = ContentUris.appendId(EventsColumns.CONTENT_URI.buildUpon(), rowId).build();
            mContext.getContentResolver().notifyChange(uri, null, true);
            return uri;
        }

        throw new SQLiteException("Failed to insert row into " + uri);
    }

    /**
     * Inserts the item
     *
     * @param uri the content uri
     * @param contentValues the content values
     */
    private Uri insertPerson(Uri uri, ContentValues contentValues) {
        long rowId = mDb.insert(PersonColumns.TABLE_NAME, PersonColumns._ID, contentValues);

        if (rowId >= 0) {
            uri = ContentUris.appendId(PersonColumns.CONTENT_URI.buildUpon(), rowId).build();
            mContext.getContentResolver().notifyChange(uri, null, true);
            return uri;
        }

        throw new SQLiteException("Failed to insert row into " + uri);
    }

    /**
     * Inserts the item
     *
     * @param uri the content uri
     * @param contentValues the content values
     */
    private Uri insertPersonEvent(Uri uri, ContentValues contentValues) {
        long rowId = mDb.insert(PersonEventsColumns.TABLE_NAME, PersonEventsColumns._ID, contentValues);

        if (rowId >= 0) {
            uri = ContentUris.appendId(PersonEventsColumns.CONTENT_URI.buildUpon(), rowId).build();
            mContext.getContentResolver().notifyChange(uri, null, true);
            return uri;
        }

        throw new SQLiteException("Failed to insert row into " + uri);
    }

    /**
     * Inserts the item
     *
     * @param uri the content uri
     * @param contentValues the content values
     */
    private Uri insertPersonMedia(Uri uri, ContentValues contentValues) {
        long rowId = mDb.insert(PersonMediaColumns.TABLE_NAME, PersonMediaColumns._ID, contentValues);

        if (rowId >= 0) {
            uri = ContentUris.appendId(PersonMediaColumns.CONTENT_URI.buildUpon(), rowId).build();
            mContext.getContentResolver().notifyChange(uri, null, true);
            return uri;
        }

        throw new SQLiteException("Failed to insert row into " + uri);
    }

    /**
     * Gets the {@link com.udacity.adcs.app.goodintents.content.AppContentProvider.UrlType} for a url.
     *
     * @param uri
     */
    private UrlType getUrlType(Uri uri) {
        return UrlType.values()[mUriMatcher.match(uri)];
    }
}
