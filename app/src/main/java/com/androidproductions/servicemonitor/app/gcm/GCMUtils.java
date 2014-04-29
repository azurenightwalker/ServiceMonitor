package com.androidproductions.servicemonitor.app.gcm;

import android.app.Activity;

import com.androidproductions.generic.lib.auth.GoogleCredentials;
import com.androidproductions.servicemonitor.app.MainActivity;
import com.androidproductions.servicemonitor.backend.registration.Registration;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public class GCMUtils extends com.androidproductions.generic.lib.gcm.GCMUtils {
    private static final String SENDER_ID = "154710985820";

    public GCMUtils(Activity context) {
        super(context, MainActivity.class.getSimpleName());
    }

    @Override
    protected void sendRegistrationIdToBackend(String regId) {
        Registration.Builder builder = new Registration.Builder(
                AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(),
                GoogleCredentials.Instance.getAccount());
        Registration service = builder.build();
        try {
            service.register(regId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getSenderId()
    {
        return SENDER_ID;
    }
}
