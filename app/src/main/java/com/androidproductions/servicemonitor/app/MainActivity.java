package com.androidproductions.servicemonitor.app;

import android.accounts.AccountManager;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.androidproductions.generic.lib.auth.GoogleCredentials;
import com.androidproductions.generic.lib.drawer.DrawerActivity;
import com.androidproductions.generic.lib.drawer.MenuOption;
import com.androidproductions.servicemonitor.app.data.ServiceStateHelper;
import com.androidproductions.servicemonitor.app.fragments.*;
import com.androidproductions.servicemonitor.app.gcm.GCMUtils;

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
                }
        );
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleCredentials.Instance.Init(getBaseContext());
        if (GoogleCredentials.Instance.isSignedIn())
            InitializeConnections();
        else
            GoogleCredentials.Instance.chooseAccount(this);
    }

    @Override
    public void CloseFragment() {
        openDrawer();
        removeFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GoogleCredentials.REQUEST_ACCOUNT_PICKER:
                if (data != null && data.getExtras() != null) {
                    GoogleCredentials.Instance.signIn(data.getExtras().getString(
                            AccountManager.KEY_ACCOUNT_NAME));
                    InitializeConnections();
                }
                break;
        }
    }

    private void InitializeConnections()
    {
        ServiceStateHelper.refreshServiceStates(this);
        GCMUtils gcm = new GCMUtils(this);
        gcm.Initialize();
    }
}
