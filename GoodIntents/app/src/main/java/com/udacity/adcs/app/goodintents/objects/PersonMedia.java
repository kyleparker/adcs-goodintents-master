package com.udacity.adcs.app.goodintents.objects;

import android.support.annotation.NonNull;

/**
 * Created by benjaminshockley on 11/9/15.
 */
public class PersonMedia implements Comparable<PersonMedia> {
    private long id;
    private long PersonId;
    private String LocalStorageURL;
    private String MediaName;

    public PersonMedia() { }

    public PersonEvent personEvent = new PersonEvent();

    public long getId() {
        return id;
    }
    public void setId(long value) {
        this.id = value;
    }

    public long getPersonId() {
        return PersonId;
    }
    public void setPersonId(long value) {
        this.PersonId = value;
    }

    public String getLocalStorageURL() {
        return LocalStorageURL;
    }
    public void setLocalStorageURL(String value) {
        this.LocalStorageURL = value == null ? "" : value.trim();
    }

    public String getMediaName() {
        return MediaName;
    }
    public void setMediaName(String value) {
        this.MediaName = value == null ? "" : value.trim();
    }


    public int compareTo(@NonNull PersonMedia another) {
        if (another == null) return 1;
        String personMediaId = String.valueOf(another.id);
        return personMediaId.compareTo(String.valueOf(id));
    }
}