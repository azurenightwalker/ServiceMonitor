package com.androidproductions.servicemonitor.app.data.services;

import android.net.Uri;

public final class ServiceStatusContract {
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String GROUP = "description";
    public static final String STATUS = "schedule";
    public static final String CLAIMANT = "claimant";
    public static final String LAST_UPDATE = "days";

    public static final Uri CONTENT_URI =
            Uri.parse("content://com.androidproductions.servicemonitor/services");

    public static final String[] PROJECTION = new String[] {
            ServiceStatusContract._ID,
            ServiceStatusContract.NAME,
            ServiceStatusContract.GROUP,
            ServiceStatusContract.STATUS,
            ServiceStatusContract.CLAIMANT,
            ServiceStatusContract.LAST_UPDATE
    };
}
