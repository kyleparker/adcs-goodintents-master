package com.udacity.adcs.app.goodintents.content;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.udacity.adcs.app.goodintents.content.layout.EventsColumns;
import com.udacity.adcs.app.goodintents.content.layout.PersonColumns;
import com.udacity.adcs.app.goodintents.content.layout.PersonEventsColumns;
import com.udacity.adcs.app.goodintents.content.layout.PersonMediaColumns;
import com.udacity.adcs.app.goodintents.object.Event;
import com.udacity.adcs.app.goodintents.object.Person;
import com.udacity.adcs.app.goodintents.object.PersonEvent;
import com.udacity.adcs.app.goodintents.object.PersonMedia;

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

    public Event getEvent(int id) {
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

    public Person getPerson(int id) {
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

    public List<Event> getEventListByType(long typeId) {
        ArrayList<Event> list = new ArrayList<>();

        Cursor cursor = mContentResolver.query(EventsColumns.CONTENT_URI, null, null, null, null);

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
            event.setDesc(cursor.getString(idxDesc));
        }
        if (idxDisplayAddress > -1) {
            event.setDisplayAddress(cursor.getString(idxDisplayAddress));
        }
        if (idxLatitude > -1) {
            event.setLatitude(cursor.getDouble(idxLatitude));
        }
        if (idxLongitude > -1) {
            event.setLongitude(cursor.getDouble(idxLongitude));
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
            personMedia.setPersonEventsId(cursor.getLong(idxPersonEventsId));
        }
        if (idxLocalStorageUrl > -1) {
            personMedia.setLocalStorageUrl(cursor.getString(idxLocalStorageUrl));
        }
        if (idxMediaName > -1) {
            personMedia.setMediaName(cursor.getString(idxMediaName));
        }

        personMedia.personEvent = createPersonEvent(cursor);

        return personMedia;
    }

    public ContentValues createContentValues(Event obj) {
        ContentValues contentValues = new ContentValues();

        // Values id < 0 indicate no id is available:
        if (obj.getId() > 0) {
            contentValues.put(EventsColumns._ID, obj.getId());
        }
        contentValues.put(EventsColumns.DATE, obj.getDate());
        contentValues.put(EventsColumns.DESC, obj.getDesc());
        contentValues.put(EventsColumns.DISPLAY_ADDRESS, obj.getDisplayName());
        contentValues.put(EventsColumns.LATITUDE, obj.getLatitude());
        contentValues.put(EventsColumns.LONGITUDE, obj.getLongitude());
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
        contentValues.put(PersonMediaColumns.LOCAL_STORAGE_URL, obj.getLocalStorageUrl());
        contentValues.put(PersonMediaColumns.MEDIA_NAME, obj.getMediaName());
        contentValues.put(PersonMediaColumns.PERSON_EVENTS_ID, obj.getPersonEventsId());

        return contentValues;
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
