package com.udacity.adcs.app.goodintents.objects;

import android.support.annotation.NonNull;

/**
 * Created by benjaminshockley on 11/9/15.
 */
public class PersonEvent implements Comparable<PersonEvent> {
    private long id;
    private long PersonId;
    private long EventId;
    private long Date;
    private long Points;

    public PersonEvent() { }

    public Person person = new Person();
    public Event event = new Event();

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

    public long getEventId() {
        return EventId;
    }
    public void setEventId(long value) {
        this.EventId = value;
    }

    public long getDate() {
        return Date;
    }
    public void setDate(long value) {
        this.Date = value;
    }

    public long getPoints() {
        return Points;
    }
    public void setPoints(long value) {
        this.Points = value;
    }

    public int compareTo(@NonNull PersonEvent another) {
        if (another == null) return 1;
        String personEventId = String.valueOf(another.id);
        return personEventId.compareTo(String.valueOf(id));
    }
}