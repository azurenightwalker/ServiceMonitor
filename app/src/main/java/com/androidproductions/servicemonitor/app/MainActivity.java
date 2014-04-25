package com.androidproductions.servicemonitor.app;

import android.app.Fragment;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.androidproductions.generic.lib.drawer.DrawerActivity;
import com.androidproductions.generic.lib.drawer.MenuOption;
import com.androidproductions.servicemonitor.app.data.ServiceStatusContract;
import com.androidproductions.servicemonitor.app.fragments.*;
import com.androidproductions.servicemonitor.app.gcm.GCMUtils;
import com.androidproductions.servicemonitor.backend.services.Services;
import com.androidproductions.servicemonitor.backend.services.model.CollectionResponseServiceRecord;
import com.androidproductions.servicemonitor.backend.services.model.ServiceRecord;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.concurrent.Callable;


public class MainActivity extends DrawerActivity implements OnFragmentInteractionListener{
    public MainActivity() {
        super(R.layout.activity_main, R.id.drawer_layout, R.id.leftList,
                R.string.app_name, R.id.conversation_detail_container,
                new MenuOption[]{
                        new MenuOption(R.string.app_name, R.drawable.ic_launcher, new Callable<Fragment>() {
                            @Override
                            public Fragment call() throws Exception {
                                return ServiceListFragment.newInstance();
                            }
                        })
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GCMUtils gcm = new GCMUtils(this);
        gcm.Initialize();
        getServices();
    }

    @Override
    public void CloseFragment() {
        openDrawer();
        removeFragment();
    }

    private void getServices() {
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
                    getContentResolver().insert(ServiceStatusContract.CONTENT_URI,AsContentValues(srv));
                    Toast.makeText(getApplicationContext(), srv.getServiceId() + srv.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(null, null, null);
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
