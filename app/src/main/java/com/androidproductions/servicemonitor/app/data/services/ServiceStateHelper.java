package com.androidproductions.servicemonitor.app.data.services;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.androidproductions.generic.lib.auth.GoogleCredentials;
import com.androidproductions.servicemonitor.app.gcm.GCMMessage;
import com.androidproductions.servicemonitor.backend.services.Services;
import com.androidproductions.servicemonitor.backend.services.model.CollectionResponseServiceRecord;
import com.androidproductions.servicemonitor.backend.services.model.ServiceRecord;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.concurrent.Callable;

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
                Services.Builder builder = new Services.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(),
                        GoogleCredentials.Instance.getAccount());
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

    public static void refreshServiceState(final Context context, final long id, final Callable<Void> callback) {
        new AsyncTask<Void, Void, ServiceRecord>() {
            @Override
            protected ServiceRecord doInBackground(Void... params) {
                Services.Builder builder = new Services.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(),
                        GoogleCredentials.Instance.getAccount());
                builder.setApplicationName("ServiceMonitor");
                try {
                    ServiceStatus ss = getServiceStatus(context, id);
                    return builder.build().get(ss.getGroup(), ss.getName()).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ServiceRecord srv) {
                context.getContentResolver()
                        .insert(ServiceStatusContract.CONTENT_URI, AsContentValues(srv));
                try {
                    callback.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(null, null, null);
    }

    public static void claimServiceState(final Context context, final long id) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Services.Builder builder = new Services.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(),
                        GoogleCredentials.Instance.getAccount());
                builder.setApplicationName("ServiceMonitor");
                try {
                    ServiceStatus ss = getServiceStatus(context, id);
                    ServiceRecord sr = new ServiceRecord();
                    sr.setClaimant(GoogleCredentials.Instance.getAccount().getSelectedAccountName());
                    sr.setStatus(ss.getStatus().Value);
                    sr.setAlerted(true);
                    builder.build().update(ss.getGroup(), ss.getName(),sr).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
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

    public static void releaseServiceState(final Context context, final long id) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Services.Builder builder = new Services.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(),
                        GoogleCredentials.Instance.getAccount());
                builder.setApplicationName("ServiceMonitor");
                try {
                    ServiceStatus ss = getServiceStatus(context, id);
                    ServiceRecord sr = new ServiceRecord();
                    sr.setClaimant(null);
                    sr.setStatus(ss.getStatus().Value);
                    sr.setAlerted(true);
                    builder.build().update(ss.getGroup(), ss.getName(),sr).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(null, null, null);
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
        cv.put(ServiceStatusContract.CLAIMANT, msg.getClaimant());
        return cv;
    }

    private static ContentValues AsContentValues(ServiceRecord srv)
    {
        ContentValues cv = new ContentValues();
        cv.put(ServiceStatusContract.NAME, srv.getServiceId());
        cv.put(ServiceStatusContract.GROUP, srv.getServiceGroup());
        cv.put(ServiceStatusContract.STATUS, srv.getStatus());
        cv.put(ServiceStatusContract.LAST_UPDATE, srv.getLastUpdate());
        cv.put(ServiceStatusContract.CLAIMANT, srv.getClaimant());
        return cv;
    }
}
