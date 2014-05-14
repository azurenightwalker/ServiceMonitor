package com.androidproductions.servicemonitor.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androidproductions.servicemonitor.app.data.servicegroups.ServiceGroupContract;
import com.androidproductions.servicemonitor.app.data.services.ServiceStatusContract;

public class ServicesDb extends SQLiteOpenHelper{
    public static final String TABLE_STATUSES = "serviceStatuses";
    public static final String TABLE_GROUPS = "serviceGroups";
    private static final String DATABASE_NAME = "ServiceMonitor";

    private static final String DATABASE_CREATE_STATUSES =
            "create table "+ TABLE_STATUSES +" (" +
                    ServiceStatusContract._ID + " integer primary key autoincrement, " +
                    ServiceStatusContract.NAME + " text not null, " +
                    ServiceStatusContract.GROUP + " text not null, " +
                    ServiceStatusContract.STATUS + " int not null, " +
                    ServiceStatusContract.CLAIMANT + " text null, " +
                    ServiceStatusContract.LAST_UPDATE + " int not null)";

    private static final String DATABASE_CREATE_GROUPS =
            "create table "+ TABLE_GROUPS +" (" +
                    ServiceGroupContract._ID + " integer primary key autoincrement, " +
                    ServiceGroupContract.NAME + " text not null, " +
                    ServiceGroupContract.SUBSCRIBED + " int not null)";

    public ServicesDb(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_STATUSES);
        sqLiteDatabase.execSQL(DATABASE_CREATE_GROUPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
