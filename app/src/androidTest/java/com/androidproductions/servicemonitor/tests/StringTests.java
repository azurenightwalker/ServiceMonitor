package com.androidproductions.servicemonitor.tests;

import android.app.Fragment;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;

import com.androidproductions.servicemonitor.app.MainActivity;
import com.androidproductions.servicemonitor.app.R;


public class StringTests extends AndroidTestCase {
    public void testAppTitle() {
        assertEquals("Service Monitor", getContext().getResources().getString(R.string.app_name));
    }
}