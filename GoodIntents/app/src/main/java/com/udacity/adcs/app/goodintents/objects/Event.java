package com.udacity.adcs.app.goodintents.objects;

import android.support.annotation.NonNull;

/**
 * Created by benjaminshockley on 11/9/15.
 */
public class Event implements Comparable<Event> {
    private long id;
    private String Name;
    private String Description;
    private String Organization;
    private long Date;
    private double Lat;
    private double Long;
    private String DisplayAddress;

    public Event() { }

    public long getId() {
        return id;
    }
    public void setId(long value) {
        this.id = value;
    }

    public String getName() {
        return Name;
    }
    public void setName(String value) {
        this.Name = value == null ? "" : value.trim();
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String value) {
        this.Description = value == null ? "" : value.trim();
    }

    public String getOrganization() {
        return Organization;
    }
    public void setOrganization(String value) {
        this.Organization = value == null ? "" : value.trim();
    }

    public long getDate() {
        return Date;
    }
    public void setDate(long value) {
        this.Date = value;
    }

    public double getLat() {
        return Lat;
    }
    public void setLat(double value) {
        this.Lat = value;
    }

    public double getLong() {
        return Long;
    }
    public void setLong(double value) {
        this.Long = value;
    }

    public String getDisplayAddress() {
        return DisplayAddress;
    }
    public void setDisplayAddress(String value) {
        this.DisplayAddress = value == null ? "" : value.trim();
    }

    public int compareTo(@NonNull Event another) {
        if (another == null) return 1;
        String eventId = String.valueOf(another.id);
        return eventId.compareTo(String.valueOf(id));
    }
}