package com.androidproductions.servicemonitor.app.data.servicegroups;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.androidproductions.generic.lib.auth.GoogleCredentials;
import com.androidproductions.servicemonitor.app.data.services.ServiceStatusContract;
import com.androidproductions.servicemonitor.backend.services.Services;
import com.androidproductions.servicemonitor.backend.services.model.CollectionResponseServiceGroupRecord;
import com.androidproductions.servicemonitor.backend.services.model.ServiceGroupRecord;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.concurrent.Callable;

public final class ServiceGroupHelper {
    public static void refreshServiceGroups(final Context context) {
        new AsyncTask<Void, Void, CollectionResponseServiceGroupRecord>() {
            @Override
            protected CollectionResponseServiceGroupRecord doInBackground(Void... params) {
                Services.Builder builder = new Services.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(),
                        GoogleCredentials.Instance.getAccount());
                builder.setApplicationName("ServiceMonitor");
                try {
                    return builder.build().listServiceGroups().execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(CollectionResponseServiceGroupRecord msg) {
                for (ServiceGroupRecord srv : msg.getItems())
                {
                    context.getContentResolver()
                            .insert(ServiceGroupContract.CONTENT_URI,AsContentValues(srv));
                }
            }
        }.execute(null, null, null);
    }

    public static ServiceGroup getServiceGroup(Context context, long id)
    {
        ServiceGroup ss = null;
        Cursor query = context.getContentResolver()
                .query(ContentUris.withAppendedId(ServiceStatusContract.CONTENT_URI, id)
                        , null, null, null, null);
        if (query != null) {
            if (query.moveToFirst())
                ss = new ServiceGroup(query);
            query.close();
        }
        return ss;
    }

    private static ContentValues AsContentValues(ServiceGroupRecord srv)
    {
        ContentValues cv = new ContentValues();
        cv.put(ServiceGroupContract.NAME, srv.getName());
        cv.put(ServiceGroupContract.SUBSCRIBED, 0);
        return cv;
    }
}
