package com.androidproductions.servicemonitor.app.data.servicegroups;


import android.database.Cursor;

public class ServiceGroup {
    private final long Id;
    private final String Name;

    public String getName() {
        return Name;
    }

    public long getId() {
        return Id;
    }

    public ServiceGroup(Cursor query) {
        Name = query.getString(query.getColumnIndex(ServiceGroupContract.NAME));
        Id = query.getLong(query.getColumnIndex(ServiceGroupContract._ID));
    }
}
