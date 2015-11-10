/*
 * Copyright (c) 2012 Ball State University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udacity.adcs.app.goodintents.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Search object
 *
 * @author Kyle Parker
 *
 */
public class Search implements Comparable<Search> {
    private String desc = "";
    private long id = -1L;
    private String name = "";
    private int orderBy = -1;
    private long typeId = -1L;

    public Person person = new Person();
    public Event event = new Event();

//    public Search() { }
//
//    private Search(Parcel in) {
//        desc = in.readString();
//        id = in.readLong();
//        name = in.readString();
//        orderBy = in.readInt();
//        typeId = in.readLong();
//
//        ClassLoader classLoader = getClass().getClassLoader();
//        person = in.readParcelable(classLoader);
//        event = in.readParcelable(classLoader);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(desc);
//        dest.writeLong(id);
//        dest.writeString(name);
//        dest.writeInt(orderBy);
//        dest.writeLong(typeId);
//
//        dest.writeParcelable(person, 0);
//        dest.writeParcelable(event, 0);
//    }
//
//    public static final Creator<Search> CREATOR = new Creator<Search>() {
//        @Override
//        public Search createFromParcel(Parcel in) {
//            return new Search(in);
//        }
//
//        @Override
//        public Search[] newArray(int size) {
//            return new Search[size];
//        }
//    };

    public String getDesc() {
        return desc;
    }
    public void setDesc(String value) {
        this.desc = value == null ? "" : value.trim();
    }

    public long getId() {
        return id;
    }
    public void setId(long value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }
    public void setName(String value) {
        this.name = value;
    }

    public int getOrderBy() {
        return orderBy;
    }
    public void setOrderBy(int value) {
        this.orderBy = value;
    }

    public long getTypeId() {
        return typeId;
    }
    public void setTypeId(long value) {
        this.typeId = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((desc == null) ? 0 : desc.hashCode());
        result = prime * result + Long.toString(id).hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + Long.toString(orderBy).hashCode();
        result = prime * result + Long.toString(typeId).hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }

        if (obj == null) { return false; }

        if (getClass() != obj.getClass()) { return false; }

        Search other = (Search) obj;

        if (desc == null) {
            if (other.desc != null) { return false; }
        } else if (!desc.equals(other.desc)) { return false; }

        if (id != other.id) { return false; }

        if (name == null) {
            if (other.name != null) { return false; }
        } else if (!name.equals(other.name)) { return false; }

        if (orderBy != other.orderBy) { return false; }

        return typeId == other.typeId;

    }

    public int compareTo(@NonNull Search another) {
        String personId = String.valueOf(another.id);
        return personId.compareTo(String.valueOf(id));
    }
}
