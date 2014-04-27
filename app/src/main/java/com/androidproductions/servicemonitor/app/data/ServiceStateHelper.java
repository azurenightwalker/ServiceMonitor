package com.androidproductions.servicemonitor.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.androidproductions.servicemonitor.app.gcm.GCMMessage;
import com.androidproductions.servicemonitor.backend.services.Services;
import com.androidproductions.servicemonitor.backend.services.model.CollectionResponseServiceRecord;
import com.androidproductions.servicemonitor.backend.services.model.ServiceRecord;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public final class ServiceStateHelper {
    public static void saveStatusFromMessage(Context context, GCMMessage msg)
    {
        context.getContentResolver()
                .insert(ServiceStatusContract.CONTENT_URI,AsContentValues(msg));
    }

    public static void refreshServiceStates(final Context context) {
        new AsyncTask<Void, Void, CollectionResponseServiceRecord>() {
            @Override
            protected CollectionResponseServiceRecord doInBackground(Void... params) {
                Services.Builder builder = new Services.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null);
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
