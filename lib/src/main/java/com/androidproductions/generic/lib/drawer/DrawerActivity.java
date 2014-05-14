package com.androidproductions.generic.lib.drawer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.widget.DrawerLayout;

import com.androidproductions.generic.lib.R;

@SuppressLint("Registered")
public class DrawerActivity extends Activity {

    private final int mContentView;
    private final int mDrawerLayoutId;
    private final int mDrawerListId;
    private final int mFragmentLocation;
    private final int mDrawerTitleId;
    private final MenuOption[] mMenuOptions;
    private Fragment mDefaultFragment;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Fragment fragment;
    private DrawerAdapter mAdapter;



    public DrawerActivity(int layout, int drawer_layout, int drawer_list, int drawer_title, int fragment_location, MenuOption[] menuOptions) {
        mContentView = layout;
        mDrawerLayoutId = drawer_layout;
        mDrawerListId = drawer_list;
        mDrawerTitleId = drawer_title;
        mFragmentLocation = fragment_location;
        mMenuOptions = menuOptions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mContentView);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mAdapter = new DrawerAdapter(this,R.layout.drawer_menu_option,mMenuOptions);

        mDrawerLayout = (DrawerLayout) findViewById(mDrawerLayoutId);
        mDrawerList = (ListView) findViewById(mDrawerListId);
        mTitle = getTitle();
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList.setAdapter(mAdapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerTitle = getResources().getString(mDrawerTitleId);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        openDrawer();
    }

    protected void setDefaultFragment(Fragment frag)
    {
        mDefaultFragment = frag;
        if (fragment == null)
            switchFragment(mDefaultFragment);
    }

    protected void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.START);
        getActionBar().setTitle(mDrawerTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void switchFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (fragment != null)
            transaction.remove(fragment);
        fragment = newFragment;
        transaction.add(mFragmentLocation, fragment,"FRAGMENT").commitAllowingStateLoss();
    }

    protected void removeFragment() {
        if (mDefaultFragment != null)
            switchFragment(mDefaultFragment);
        else {
            if (fragment != null)
                getFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            setTitle(mDrawerTitle);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    public void selectItem(final int position) {
        mDrawerList.setItemChecked(position, true);
        setTitle(mAdapter.getName(position));
        switchFragment(mAdapter.getFragment(position));
        closeDrawer();
    }

    protected void closeDrawer() {
        final Runnable r = new Runnable()
        {
            public void run()
            {
                mDrawerLayout.closeDrawer(Gravity.START);
            }
        };
        Handler handler = new Handler();
        handler.post(r);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
