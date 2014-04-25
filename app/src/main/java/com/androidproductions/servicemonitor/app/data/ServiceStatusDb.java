package com.androidproductions.servicemonitor.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ServiceStatusDb extends SQLiteOpenHelper{
    public static final String TABLE_STATUSES = "serviceStatuses";
    private static final String DATABASE_NAME = "ServiceMonitor";

    private static final String DATABASE_CREATE_STATUSES =
            "create table "+ TABLE_STATUSES +" (" +
                    ServiceStatusContract._ID + " integer primary key autoincrement, " +
                    ServiceStatusContract.NAME + " text not null, " +
                    ServiceStatusContract.GROUP + " text not null, " +
                    ServiceStatusContract.STATUS + " int not null, " +
                    ServiceStatusContract.LAST_UPDATE + " int not null)";

    public ServiceStatusDb(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_STATUSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
