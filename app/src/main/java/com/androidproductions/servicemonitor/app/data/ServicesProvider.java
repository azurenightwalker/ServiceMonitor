package com.androidproductions.servicemonitor.app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.androidproductions.servicemonitor.app.data.servicegroups.ServiceGroupContract;
import com.androidproductions.servicemonitor.app.data.services.ServiceStatusContract;

public class ServicesProvider extends ContentProvider
{
    private static final int SERVICE = 0;
    private static final int SERVICE_ID = 1;
    private static final int SERVICE_GROUP = 2;
    private static final int SERVICE_GROUP_ID = 3;

    private static final String PROVIDER_NAME =
            "com.androidproductions.servicemonitor";
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(PROVIDER_NAME, "services", SERVICE);
        uriMatcher.addURI(PROVIDER_NAME, "services/#", SERVICE_ID);
        uriMatcher.addURI(PROVIDER_NAME, "servicegroups", SERVICE_GROUP);
        uriMatcher.addURI(PROVIDER_NAME, "servicegroups/#", SERVICE_GROUP_ID);
    }

    private ServicesDb mServicesDb;

    public boolean onCreate() {
        mServicesDb = new ServicesDb(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Default projection if none supplied
        if(projection == null) projection = getDefaultProjection(uri);

        // Defaults & ID's
        switch(uriMatcher.match(uri))
        {
            case SERVICE:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;
            case SERVICE_ID:
                selection = (selection == null ? "" : (selection + " ")) +
                        ServiceStatusContract._ID + " = " + uri.getLastPathSegment();
                break;
            case SERVICE_GROUP:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;
            case SERVICE_GROUP_ID:
                selection = (selection == null ? "" : (selection + " ")) +
                        ServiceGroupContract._ID + " = " + uri.getLastPathSegment();
                break;
            default:
                return null;
        }
        final SQLiteDatabase db = mServicesDb.getReadableDatabase();
        if (db != null) {
            Cursor c = db.query(findTableName(uri), projection, selection, selectionArgs, null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        }
        return null;
    }

    public String getType(Uri uri) {
        switch(uriMatcher.match(uri))
        {
            case SERVICE:
                return "vnd.android.cursor.dir/vnd.com.androidproductions.servicemonitor.service";
            case SERVICE_ID:
                return "vnd.android.cursor.item/vnd.com.androidproductions.servicemonitor.service";
            case SERVICE_GROUP:
                return "vnd.android.cursor.dir/vnd.com.androidproductions.servicemonitor.servicegroup";
            case SERVICE_GROUP_ID:
                return "vnd.android.cursor.item/vnd.com.androidproductions.servicemonitor.servicegroup";
            default:
                return null;
        }
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mServicesDb.getWritableDatabase();
        if (db != null) {
            Cursor c = query(uri,null,ServiceStatusContract.NAME + " = ?",
                    new String[] {contentValues.getAsString(ServiceStatusContract.NAME)},null);
            if (c.moveToFirst())
                contentValues.put(ServiceStatusContract._ID,
                        c.getLong(c.getColumnIndex(ServiceStatusContract._ID)));
            long id = db.insertWithOnConflict(findTableName(uri), "", contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE);
            Uri itemUri = ContentUris.withAppendedId(uri,id);
            getContext().getContentResolver().notifyChange(itemUri, null);
            return itemUri;
        }
        return null;
    }
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mServicesDb.getWritableDatabase();
        if (db != null) {
            return db.delete(findTableName(uri),selection,selectionArgs);
        }
        return 0;
    }

    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mServicesDb.getWritableDatabase();
        if (db != null) {
            return db.update(findTableName(uri),contentValues,selection,selectionArgs);
        }
        return 0;
    }

    private String findTableName(Uri uri)
    {
        switch(uriMatcher.match(uri))
        {
            case SERVICE:
                return ServicesDb.TABLE_STATUSES;
            case SERVICE_ID:
                return ServicesDb.TABLE_STATUSES;
            case SERVICE_GROUP:
                return ServicesDb.TABLE_GROUPS;
            case SERVICE_GROUP_ID:
                return ServicesDb.TABLE_GROUPS;
            default:
                return null;
        }
    }

    private String[] getDefaultProjection(Uri uri)
    {
        switch(uriMatcher.match(uri))
        {
            case SERVICE:
                return ServiceStatusContract.PROJECTION;
            case SERVICE_ID:
                return ServiceStatusContract.PROJECTION;
            case SERVICE_GROUP:
                return ServiceGroupContract.PROJECTION;
            case SERVICE_GROUP_ID:
                return ServiceGroupContract.PROJECTION;
            default:
                return null;
        }
    }
}
