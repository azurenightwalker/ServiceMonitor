package com.androidproductions.servicemonitor.app.data.servicegroups;

import android.net.Uri;

public final class ServiceGroupContract {
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String SUBSCRIBED = "subscribed";

    public static final Uri CONTENT_URI =
            Uri.parse("content://com.androidproductions.servicemonitor/servicegroups");

    public static final String[] PROJECTION = new String[] {
            ServiceGroupContract._ID,
            ServiceGroupContract.NAME,
            ServiceGroupContract.SUBSCRIBED
    };
}
