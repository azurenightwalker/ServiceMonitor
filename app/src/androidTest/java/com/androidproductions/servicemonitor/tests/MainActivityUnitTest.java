package com.androidproductions.servicemonitor.tests;

import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.androidproductions.servicemonitor.app.DetailsActivity;
import com.androidproductions.servicemonitor.app.MainActivity;
import com.androidproductions.servicemonitor.app.R;


public class MainActivityUnitTest extends
        android.test.SingleLaunchActivityTestCase <MainActivity> {

    private MainActivity activity;

    public MainActivityUnitTest() {
        super("com.androidproductions.servicemonitor.app",MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    public void testCorrectSetup() {
        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable() {
            public void run()
            {
                activity.CloseFragment();
            }
        });
        assertEquals("Service Monitor", activity.getActionBar().getTitle());
    }

    public void testSelectFragment() {
        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable() {
            public void run()
            {
                activity.selectItem(0);
            }
        });
        waitForFragment("FRAGMENT", 10000);
        assertEquals("Monitor", activity.getActionBar().getTitle());
    }

    protected Fragment waitForFragment(String tag, int timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() <= endTime) {

            Fragment fragment = getActivity().getFragmentManager().findFragmentByTag(tag);
            if (fragment != null) {
                return fragment;
            }
        }
        return null;
    }
}