package com.udacity.adcs.app.goodintents.content;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.udacity.adcs.app.goodintents.content.layout.EventsColumns;
import com.udacity.adcs.app.goodintents.content.layout.PersonColumns;
import com.udacity.adcs.app.goodintents.content.layout.PersonEventsColumns;
import com.udacity.adcs.app.goodintents.content.layout.PersonMediaColumns;
import com.udacity.adcs.app.goodintents.content.layout.SearchColumns;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.objects.Person;
import com.udacity.adcs.app.goodintents.objects.PersonEvent;
import com.udacity.adcs.app.goodintents.objects.PersonMedia;
import com.udacity.adcs.app.goodintents.objects.Search;
import com.udacity.adcs.app.goodintents.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for the content provider
 *
 * Created by kyleparker on 11/5/2015.
 */
public class AppProviderUtils {
    private final ContentResolver mContentResolver;

    public AppProviderUtils(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public Event getEvent(long id) {
        Uri uri = Uri.parse(EventsColumns.CONTENT_URI + "/" + id);

        Cursor cursor = mContentResolver.query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return createEvent(cursor);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public List<Event> getEventList() {
        ArrayList<Event> list = new ArrayList<>();

        Cursor cursor = mContentResolver.query(EventsColumns.CONTENT_URI, null, null, null, EventsColumns.DATE + " DESC");

        if (cursor != null) {
            list.ensureCapacity(cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    list.add(createEvent(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return list;
    }

    public List<PersonEvent> getEventListByType(long typeId) {
        ArrayList<PersonEvent> list = new ArrayList<>();

        Uri uri = Uri.parse(PersonEventsColumns.CONTENT_URI_BY_TYPE_ID + "/" + typeId);
        Cursor cursor = mContentResolver.query(uri, getPersonEventProjection(), null, null, null);

        if (cursor != null) {
            list.ensureCapacity(cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    list.add(createPersonEvent(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return list;
    }

    public List<PersonEvent> getEventListByEvent(long eventId, long typeId) {
        ArrayList<PersonEvent> list = new ArrayList<>();

        Uri uri = Uri.parse(PersonEventsColumns.CONTENT_URI_BY_TYPE_ID + "/" + typeId);

        String selection = EventsColumns.TABLE_NAME + "." + EventsColumns._ID + " = ? ";
        String[] selectionArgs = new String[] { String.valueOf(eventId) };

        Cursor cursor = mContentResolver.query(uri, getPersonEventProjection(), null, null, null);

        if (cursor != null) {
            list.ensureCapacity(cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    list.add(createPersonEvent(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return list;
    }

    public List<PersonMedia> getMediaByPersonEvent(long personId, long eventId) {
        ArrayList<PersonMedia> list = new ArrayList<>();

        Uri uri = Uri.parse(PersonMediaColumns.CONTENT_URI_BY_PERSON_EVENT + "/" + personId);

        String selection = PersonColumns.TABLE_NAME + "." + PersonColumns._ID + " = ? " +
                " AND " +
                EventsColumns.TABLE_NAME + "." + EventsColumns._ID + " = ? ";
        String[] selectionArgs = new String[] { String.valueOf(personId), String.valueOf(eventId) };

        Cursor cursor = mContentResolver.query(uri, getPersonEventProjection(), selection, selectionArgs, null);

        if (cursor != null) {
            list.ensureCapacity(cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    list.add(createPersonMedia(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return list;
    }

    public Person getPerson(long id) {
        Uri uri = Uri.parse(PersonColumns.CONTENT_URI + "/" + id);

        Cursor cursor = mContentResolver.query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return createPerson(cursor);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public Person getPersonByGoogleId(String googleAccountId) {
        if (TextUtils.isEmpty(googleAccountId)) {
            return null;
        }

        Uri uri = Uri.parse(PersonColumns.CONTENT_URI_BY_GOOGLE_ACCOUNT + "/" + Uri.encode(googleAccountId));

        Cursor cursor = mContentResolver.query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return createPerson(cursor);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public List<Person> getPersonList() {
        ArrayList<Person> list = new ArrayList<>();

        Cursor cursor = mContentResolver.query(PersonColumns.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            list.ensureCapacity(cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    list.add(createPerson(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return list;
    }

    public Search getSearch(long id, long typeId) {

        Uri uri = Uri.parse(SearchColumns.CONTENT_URI + "/" + id);

        String selection = SearchColumns.TABLE_NAME + "." + SearchColumns.TYPE_ID + " = ? ";
        String[] selectionArgs = new String[] { String.valueOf(typeId) };

        Cursor cursor = mContentResolver.query(uri, null, selection, selectionArgs, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return createSearch(cursor);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public List<Search> getSearchList(String query) {
        if (TextUtils.isEmpty(query)) {
            return null;
        }

        String selection = SearchColumns.TABLE_NAME + " MATCH ?";
        String[] selectionArgs = new String[] { query };

        ArrayList<Search> search = new ArrayList<>();

        Cursor cursor = mContentResolver.query(SearchColumns.CONTENT_URI, null, selection, selectionArgs, SearchColumns.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            search.ensureCapacity(cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    search.add(createSearch(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return search;
    }

    public Uri insertEvent(Event obj) {
        Search s = getSearch(obj.getId(), Constants.Type.EVENT);
        Search search = createSearch(obj.getId(), obj.getName(), obj.getDescription(), 1,
                Constants.Type.EVENT);

        if (s == null) {
            insertSearch(search);
        } else {
            updateSearch(search);
        }

        return mContentResolver.insert(EventsColumns.CONTENT_URI, createContentValues(obj));
    }

    public Uri insertPerson(Person obj) {
        Search s = getSearch(obj.getId(), Constants.Type.PERSON);
        Search search = createSearch(obj.getId(), obj.getDisplayName(), obj.getEmailAddress(), 2,
                Constants.Type.PERSON);

        if (s == null) {
            insertSearch(search);
        } else {
            updateSearch(search);
        }

        return mContentResolver.insert(PersonColumns.CONTENT_URI, createContentValues(obj));
    }

    public Uri insertPersonEvent(PersonEvent obj) {
        return mContentResolver.insert(PersonEventsColumns.CONTENT_URI, createContentValues(obj));
    }

    public Uri insertSearch(Search obj) {
        return mContentResolver.insert(SearchColumns.CONTENT_URI, createContentValues(obj));
    }

    public void updateEvent(Event obj) {
        Search s = getSearch(obj.getId(), Constants.Type.EVENT);
        Search search = createSearch(obj.getId(), obj.getName(), obj.getDescription(), 1,
                Constants.Type.EVENT);

        if (s == null) {
            insertSearch(search);
        } else {
            updateSearch(search);
        }

        mContentResolver.update(EventsColumns.CONTENT_URI, createContentValues(obj),
                EventsColumns._ID + "=" + obj.getId(), null);
    }

    public void updatePerson(Person obj) {
        Search s = getSearch(obj.getId(), Constants.Type.PERSON);
        Search search = createSearch(obj.getId(), obj.getDisplayName(), obj.getEmailAddress(), 2,
                Constants.Type.PERSON);

        if (s == null) {
            insertSearch(search);
        } else {
            updateSearch(search);
        }

        mContentResolver.update(PersonColumns.CONTENT_URI, createContentValues(obj),
                PersonColumns.GOOGLE_ACCOUNT_ID + "='" + obj.getGoogleAccountId() + "'", null);
    }

    public void updateSearch(Search obj) {
        String where = SearchColumns.ITEM_ID + "=" + obj.getId() +
                " AND " + SearchColumns.TYPE_ID + "=" + obj.getTypeId();

        mContentResolver.update(SearchColumns.CONTENT_URI, createContentValues(obj), where, null);
    }

    /**
     *
     * @param cursor
     * @return
     */
    private Event createEvent(Cursor cursor) {
        int idxId = cursor.getColumnIndex(EventsColumns._ID);
        int idxDate = cursor.getColumnIndex(EventsColumns.DATE);
        int idxDesc = cursor.getColumnIndex(EventsColumns.DESC);
        int idxDisplayAddress = cursor.getColumnIndex(EventsColumns.DISPLAY_ADDRESS);
        int idxLatitude = cursor.getColumnIndex(EventsColumns.LATITUDE);
        int idxLongitude = cursor.getColumnIndex(EventsColumns.LONGITUDE);
        int idxName = cursor.getColumnIndex(EventsColumns.NAME);
        int idxOrganization = cursor.getColumnIndex(EventsColumns.ORGANIZATION);

        Event event = new Event();

        if (idxId > -1) {
            event.setId(cursor.getLong(idxId));
        }
        if (idxDate > -1) {
            event.setDate(cursor.getLong(idxDate));
        }
        if (idxDesc > -1) {
            event.setDescription(cursor.getString(idxDesc));
        }
        if (idxDisplayAddress > -1) {
            event.setDisplayAddress(cursor.getString(idxDisplayAddress));
        }
        if (idxLatitude > -1) {
            event.setLat(cursor.getDouble(idxLatitude));
        }
        if (idxLongitude > -1) {
            event.setLong(cursor.getDouble(idxLongitude));
        }
        if (idxName > -1) {
            event.setName(cursor.getString(idxName));
        }
        if (idxOrganization > -1) {
            event.setOrganization(cursor.getString(idxOrganization));
        }

        return event;
    }

    /**
     *
     * @param cursor
     * @return
     */
    private Person createPerson(Cursor cursor) {
        int idxId = cursor.getColumnIndex(PersonColumns._ID);
        int idxDisplayName = cursor.getColumnIndex(PersonColumns.DISPLAY_NAME);
        int idxEmailAddress = cursor.getColumnIndex(PersonColumns.EMAIL_ADDRESS);
        int idxPhotoUrl = cursor.getColumnIndex(PersonColumns.PHOTO_URL);
        int idxTypeId = cursor.getColumnIndex(PersonColumns.TYPE_ID);

        Person person = new Person();

        if (idxId > -1) {
            person.setId(cursor.getLong(idxId));
        }
        if (idxDisplayName > -1) {
            person.setDisplayName(cursor.getString(idxDisplayName));
        }
        if (idxEmailAddress > -1) {
            person.setEmailAddress(cursor.getString(idxEmailAddress));
        }
        if (idxPhotoUrl > -1) {
            person.setPhotoUrl(cursor.getString(idxPhotoUrl));
        }
        if (idxTypeId > -1) {
            person.setTypeId(cursor.getLong(idxTypeId));
        }

        return person;
    }

    /**
     *
     * @param cursor
     * @return
     */
    private PersonEvent createPersonEvent(Cursor cursor) {
        int idxId = cursor.getColumnIndex(PersonEventsColumns._ID);
        int idxPersonId = cursor.getColumnIndex(PersonEventsColumns.PERSON_ID);
        int idxEventId = cursor.getColumnIndex(PersonEventsColumns.EVENT_ID);
        int idxDate = cursor.getColumnIndex(PersonEventsColumns.DATE);
        int idxPoints = cursor.getColumnIndex(PersonEventsColumns.POINTS);

        PersonEvent personEvents = new PersonEvent();

        if (idxId > -1) {
            personEvents.setId(cursor.getLong(idxId));
        }
        if (idxPersonId > -1) {
            personEvents.setPersonId(cursor.getLong(idxPersonId));
        }
        if (idxEventId > -1) {
            personEvents.setEventId(cursor.getLong(idxEventId));
        }
        if (idxDate > -1) {
            personEvents.setDate(cursor.getLong(idxDate));
        }
        if (idxPoints > -1) {
            personEvents.setPoints(cursor.getLong(idxPoints));
        }

        personEvents.person = createPerson(cursor);
        personEvents.event = createEvent(cursor);

        return personEvents;
    }

    /**
     *
     * @param cursor
     * @return
     */
    private PersonMedia createPersonMedia(Cursor cursor) {
        int idxId = cursor.getColumnIndex(PersonMediaColumns._ID);
        int idxPersonEventsId = cursor.getColumnIndex(PersonMediaColumns.PERSON_EVENTS_ID);
        int idxLocalStorageUrl = cursor.getColumnIndex(PersonMediaColumns.LOCAL_STORAGE_URL);
        int idxMediaName = cursor.getColumnIndex(PersonMediaColumns.MEDIA_NAME);

        PersonMedia personMedia = new PersonMedia();

        if (idxId > -1) {
            personMedia.setId(cursor.getInt(idxId));
        }
        if (idxPersonEventsId > -1) {
            personMedia.setPersonEventId(cursor.getLong(idxPersonEventsId));
        }
        if (idxLocalStorageUrl > -1) {
            personMedia.setLocalStorageURL(cursor.getString(idxLocalStorageUrl));
        }
        if (idxMediaName > -1) {
            personMedia.setMediaName(cursor.getString(idxMediaName));
        }

        personMedia.personEvent = createPersonEvent(cursor);

        return personMedia;
    }

    /**
     * Create a basic search object based on the incoming data
     *
     * @param id
     * @param name
     * @param desc
     * @param orderBy
     * @param typeId
     * @return
     */
    private Search createSearch(long id, String name, String desc, int orderBy, long typeId) {
        Search search = new Search();
        search.setId(id);
        search.setName(name);
        search.setDesc(desc);
        search.setOrderBy(orderBy);
        search.setTypeId(typeId);

        return search;
    }

    /**
     *
     * @param cursor
     * @return
     */
    private Search createSearch(Cursor cursor) {
        int idxDesc = cursor.getColumnIndex(SearchColumns.DESC);
        int idxId = cursor.getColumnIndex(SearchColumns.ITEM_ID);
        int idxName = cursor.getColumnIndex(SearchColumns.NAME);
        int idxOrderBy = cursor.getColumnIndex(SearchColumns.ORDER_BY);
        int idxTypeId = cursor.getColumnIndex(SearchColumns.TYPE_ID);

        Search search = new Search();

        if (idxDesc > -1) {
            search.setDesc(cursor.getString(idxDesc));
        }
        if (idxId > -1) {
            search.setId(cursor.getLong(idxId));
        }
        if (idxName > -1) {
            search.setName(cursor.getString(idxName));
        }
        if (idxOrderBy > -1) {
            search.setOrderBy(cursor.getInt(idxOrderBy));
        }
        if (idxTypeId > -1) {
            search.setTypeId(cursor.getLong(idxTypeId));
        }

        search.person = createPerson(cursor);
        search.event = createEvent(cursor);

        return search;
    }

    public ContentValues createContentValues(Event obj) {
        ContentValues contentValues = new ContentValues();

        // Values id < 0 indicate no id is available:
        if (obj.getId() > 0) {
            contentValues.put(EventsColumns._ID, obj.getId());
        }
        contentValues.put(EventsColumns.DATE, obj.getDate());
        contentValues.put(EventsColumns.DESC, obj.getDescription());
        contentValues.put(EventsColumns.DISPLAY_ADDRESS, obj.getDisplayAddress());
        contentValues.put(EventsColumns.LATITUDE, obj.getLat());
        contentValues.put(EventsColumns.LONGITUDE, obj.getLong());
        contentValues.put(EventsColumns.NAME, obj.getName());
        contentValues.put(EventsColumns.ORGANIZATION, obj.getOrganization());

        return contentValues;
    }

    public ContentValues createContentValues(Person obj) {
        ContentValues contentValues = new ContentValues();

        // Values id < 0 indicate no id is available:
        if (obj.getId() > 0) {
            contentValues.put(PersonColumns._ID, obj.getId());
        }
        contentValues.put(PersonColumns.DISPLAY_NAME, obj.getDisplayName());
        contentValues.put(PersonColumns.EMAIL_ADDRESS, obj.getEmailAddress());
        contentValues.put(PersonColumns.PHOTO_URL, obj.getPhotoUrl());
        contentValues.put(PersonColumns.TYPE_ID, obj.getTypeId());

        return contentValues;
    }

    public ContentValues createContentValues(PersonEvent obj) {
        ContentValues contentValues = new ContentValues();

        // Values id < 0 indicate no id is available:
        if (obj.getId() > 0) {
            contentValues.put(PersonEventsColumns._ID, obj.getId());
        }
        contentValues.put(PersonEventsColumns.DATE, obj.getDate());
        contentValues.put(PersonEventsColumns.EVENT_ID, obj.getEventId());
        contentValues.put(PersonEventsColumns.PERSON_ID, obj.getPersonId());
        contentValues.put(PersonEventsColumns.POINTS, obj.getPoints());

        return contentValues;
    }

    public ContentValues createContentValues(PersonMedia obj) {
        ContentValues contentValues = new ContentValues();

        // Values id < 0 indicate no id is available:
        if (obj.getId() > 0) {
            contentValues.put(PersonMediaColumns._ID, obj.getId());
        }
        contentValues.put(PersonMediaColumns.LOCAL_STORAGE_URL, obj.getLocalStorageURL());
        contentValues.put(PersonMediaColumns.MEDIA_NAME, obj.getMediaName());
        contentValues.put(PersonMediaColumns.PERSON_EVENTS_ID, obj.getPersonEventId());

        return contentValues;
    }

    /**
     *
     * @param obj
     * @return
     */
    private ContentValues createContentValues(Search obj) {
        ContentValues values = new ContentValues();

        values.put(SearchColumns.DESC, obj.getDesc());
        values.put(SearchColumns.ITEM_ID, obj.getId());
        values.put(SearchColumns.NAME, obj.getName());
        values.put(SearchColumns.ORDER_BY, obj.getOrderBy());
        values.put(SearchColumns.TYPE_ID, obj.getTypeId());

        return values;
    }

    private String[] getPersonEventProjection() {
        return new String[] {
                PersonEventsColumns.TABLE_NAME + "." + PersonEventsColumns._ID,
                PersonEventsColumns.TABLE_NAME + "." + PersonEventsColumns.DATE,
                PersonEventsColumns.TABLE_NAME + "." + PersonEventsColumns.EVENT_ID,
                PersonEventsColumns.TABLE_NAME + "." + PersonEventsColumns.PERSON_ID,
                PersonEventsColumns.TABLE_NAME + "." + PersonEventsColumns.POINTS,
                PersonColumns.TABLE_NAME + "." + PersonColumns.DISPLAY_NAME,
                PersonColumns.TABLE_NAME + "." + PersonColumns.EMAIL_ADDRESS,
                PersonColumns.TABLE_NAME + "." + PersonColumns.PHOTO_URL,
                PersonColumns.TABLE_NAME + "." + PersonColumns.TYPE_ID,
                EventsColumns.TABLE_NAME + "." + EventsColumns.DATE,
                EventsColumns.TABLE_NAME + "." + EventsColumns.DESC,
                EventsColumns.TABLE_NAME + "." + EventsColumns.DISPLAY_ADDRESS,
                EventsColumns.TABLE_NAME + "." + EventsColumns.LATITUDE,
                EventsColumns.TABLE_NAME + "." + EventsColumns.LONGITUDE,
                EventsColumns.TABLE_NAME + "." + EventsColumns.NAME,
                EventsColumns.TABLE_NAME + "." + EventsColumns.ORGANIZATION
        };
    }

    /**
     * A factory which can produce instances of {@link AppProviderUtils}
     */
    public static class Factory {
        private static Factory instance = new Factory();

        /**
         * Creates and returns an instance of {@link AppProviderUtils} which uses the given context to access its data.
         */
        public static AppProviderUtils get(Context context) {
            return instance.newForContext(context);
        }

        /**
         * Creates an instance of {@link AppProviderUtils}.
         */
        protected AppProviderUtils newForContext(Context context) {
            return new AppProviderUtils(context.getContentResolver());
        }
    }
}
