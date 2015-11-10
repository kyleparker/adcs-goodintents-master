package com.udacity.adcs.app.goodintents.objects;

import android.support.annotation.NonNull;

/**
 * Created by benjaminshockley on 11/9/15.
 */
public class Type implements Comparable<Type> {
    private long id;
    private String TypeName;

    public Type() { }

    public long getId() {
        return id;
    }
    public void setId(long value) {
        this.id = value;
    }

    public String getTypeName() {
        return TypeName;
    }
    public void setTypeName(String value) {
        this.TypeName = value == null ? "" : value.trim();
    }

    public int compareTo(@NonNull Type another) {
        if (another == null) return 1;
        String typeId = String.valueOf(another.id);
        return typeId.compareTo(String.valueOf(id));
    }
}