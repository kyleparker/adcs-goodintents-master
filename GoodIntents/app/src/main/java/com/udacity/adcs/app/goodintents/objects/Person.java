package com.udacity.adcs.app.goodintents.objects;

import android.support.annotation.NonNull;

/**
 * Person object
 *
 * @author Kyle Parker
 *
 */
public class Person implements Comparable<Person> {
    private long id;
    private String displayName;
    private String emailAddress;
    private String googleAccountId;
    private String photoUrl;
    private long typeId;

    public Person() { }

    public long getId() {
        return id;
    }
    public void setId(long value) {
        this.id = value;
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String value) {
        this.displayName = value == null ? "" : value.trim();
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String value) {
        this.emailAddress = value == null ? "" : value.trim();
    }

    public String getGoogleAccountId() {
        return googleAccountId;
    }
    public void setGoogleAccountId(String value) {
        this.googleAccountId = value == null ? "" : value.trim();
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String value) {
        this.photoUrl = value == null ? "" : value.trim();
    }

    public long getTypeId() {
        return typeId;
    }
    public void setTypeId(long value) {
        this.typeId = value;
    }

    public int compareTo(@NonNull Person another) {
        if (another == null) return 1;
        String personId = String.valueOf(another.id);
        return personId.compareTo(String.valueOf(id));
    }
}
