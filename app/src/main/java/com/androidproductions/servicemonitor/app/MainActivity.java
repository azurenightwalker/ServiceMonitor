package com.androidproductions.servicemonitor.app;

import android.app.Fragment;

import com.androidproductions.generic.lib.drawer.DrawerActivity;
import com.androidproductions.generic.lib.drawer.MenuOption;
import com.androidproductions.servicemonitor.app.fragments.*;

import java.util.concurrent.Callable;


public class MainActivity extends DrawerActivity implements OnFragmentInteractionListener{

    public MainActivity() {
        super(R.layout.activity_main, R.id.drawer_layout, R.id.leftList,
                R.string.app_name, R.id.conversation_detail_container,
                new MenuOption[]{
                        new MenuOption(R.string.app_name, R.drawable.ic_launcher, new Callable<Fragment>() {
                            @Override
                            public Fragment call() throws Exception {
                                return EmptyFragment.newInstance();
                            }
                        })
                });
    }

    @Override
    public void CloseFragment() {
        openDrawer();
        removeFragment();
    }
}
