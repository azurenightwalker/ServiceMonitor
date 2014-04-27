package com.androidproductions.servicemonitor.app;

import android.app.Fragment;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.androidproductions.generic.lib.drawer.DrawerActivity;
import com.androidproductions.generic.lib.drawer.MenuOption;
import com.androidproductions.servicemonitor.app.data.ServiceStateHelper;
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
        ServiceStateHelper.refreshServiceStates(this);
    }

    @Override
    public void CloseFragment() {
        openDrawer();
        removeFragment();
    }


}
