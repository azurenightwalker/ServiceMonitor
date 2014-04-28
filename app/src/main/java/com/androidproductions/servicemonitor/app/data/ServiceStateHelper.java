package com.androidproductions.servicemonitor.app.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.androidproductions.servicemonitor.app.gcm.GCMMessage;
import com.androidproductions.servicemonitor.backend.services.Services;
import com.androidproductions.servicemonitor.backend.services.model.CollectionResponseServiceRecord;
import com.androidproductions.servicemonitor.backend.services.model.ServiceRecord;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.io.IOException;

public final class ServiceStateHelper {
    public static void saveStatusFromMessage(Context context, GCMMessage msg)
    {
        context.getContentResolver()
                .insert(ServiceStatusContract.CONTENT_URI,AsContentValues(msg));
    }

    public static void refreshServiceStates(final Context context, final GoogleAccountCredential credential) {
        new AsyncTask<Void, Void, CollectionResponseServiceRecord>() {
            @Override
            protected CollectionResponseServiceRecord doInBackground(Void... params) {
                Services.Builder builder = new Services.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), credential);
                builder.setApplicationName("ServiceMonitor");
                try {
                    return builder.build().listServices("test").execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(CollectionResponseServiceRecord msg) {
                for (ServiceRecord srv : msg.getItems())
                {
                    context.getContentResolver()
                            .insert(ServiceStatusContract.CONTENT_URI,AsContentValues(srv));
                }
            }
        }.execute(null, null, null);
    }

    public static ServiceStatus getServiceStatus(Context context, long id)
    {
        ServiceStatus ss = null;
        Cursor query = context.getContentResolver()
                .query(ContentUris.withAppendedId(ServiceStatusContract.CONTENT_URI, id)
                        , null, null, null, null);
        if (query != null) {
            if (query.moveToFirst())
                ss = new ServiceStatus(query);
            query.close();
        }
        return ss;
    }

    public static ServiceStatus getServiceStatus(Context context, String name)
    {
        ServiceStatus ss = null;
        Cursor query = context.getContentResolver()
                .query(ServiceStatusContract.CONTENT_URI
                        , null, ServiceStatusContract.NAME + " = ?", new String[] { name }, null);
        if (query != null) {
            if (query.moveToFirst())
                ss = new ServiceStatus(query);
            query.close();
        }
        return ss;
    }

    private static ContentValues AsContentValues(GCMMessage msg)
    {
        ContentValues cv = new ContentValues();
        cv.put(ServiceStatusContract.NAME, msg.getServiceId());
        cv.put(ServiceStatusContract.GROUP, msg.getServiceGroup());
        cv.put(ServiceStatusContract.STATUS, msg.getStatus());
        cv.put(ServiceStatusContract.LAST_UPDATE, msg.getLastUpdate());
        return cv;
    }

    private static ContentValues AsContentValues(ServiceRecord srv)
    {
        ContentValues cv = new ContentValues();
        cv.put(ServiceStatusContract.NAME, srv.getServiceId());
        cv.put(ServiceStatusContract.GROUP, srv.getServiceGroup());
        cv.put(ServiceStatusContract.STATUS, srv.getStatus());
        cv.put(ServiceStatusContract.LAST_UPDATE, srv.getLastUpdate());
        return cv;
    }
}
