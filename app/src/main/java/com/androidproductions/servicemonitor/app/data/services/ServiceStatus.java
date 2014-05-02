package com.androidproductions.servicemonitor.app.data.services;


import android.database.Cursor;

public class ServiceStatus {
    private final long Id;
    private final String Name;
    private final String Group;
    private final String Claimant;
    private final long LastUpdate;

    public ServiceState getStatus() {
        return Status;
    }

    public long getLastUpdate() {
        return LastUpdate;
    }

    public String getGroup() {
        return Group;
    }

    public String getName() {
        return Name;
    }

    public long getId() {
        return Id;
    }

    private final ServiceState Status;

    public ServiceStatus(Cursor query) {
        Name = query.getString(query.getColumnIndex(ServiceStatusContract.NAME));
        Group = query.getString(query.getColumnIndex(ServiceStatusContract.GROUP));
        LastUpdate = query.getLong(query.getColumnIndex(ServiceStatusContract.LAST_UPDATE));
        Status = ServiceState.parse(query.getInt(query.getColumnIndex(ServiceStatusContract.STATUS)));
        Id = query.getLong(query.getColumnIndex(ServiceStatusContract._ID));
        Claimant = query.getString(query.getColumnIndex(ServiceStatusContract.CLAIMANT));
    }

    public String getClaimant() {
        return Claimant;
    }
}
