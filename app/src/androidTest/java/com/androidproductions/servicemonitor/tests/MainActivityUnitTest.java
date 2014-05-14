package com.androidproductions.servicemonitor.tests;

import android.app.Fragment;
import android.content.Intent;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.androidproductions.servicemonitor.app.DetailsActivity;
import com.androidproductions.servicemonitor.app.MainActivity;
import com.androidproductions.servicemonitor.app.R;


public class MainActivityUnitTest extends
        android.test.ActivityInstrumentationTestCase2 <MainActivity> {

    private int buttonId;
    private MainActivity activity;

    public MainActivityUnitTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    public void testCorrectSetup() {
        assertEquals("Service Monitor", activity.getActionBar().getTitle());
    }
}