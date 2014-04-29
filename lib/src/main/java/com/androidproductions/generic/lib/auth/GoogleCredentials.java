package com.androidproductions.generic.lib.auth;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public enum GoogleCredentials {
    Instance;
    public static final int REQUEST_ACCOUNT_PICKER = 999;

    private SharedPreferences settings;

    public GoogleAccountCredential getAccount() {
        return Account;
    }

    private GoogleAccountCredential Account;

    public void Init(Context activity)
    {
        settings = activity.getSharedPreferences("GoogleAccount", 0);
        Account = GoogleAccountCredential.usingAudience(activity, "server:client_id:154710985820-grgcf905ln13tno9g93qlusu18i5ievc.apps.googleusercontent.com");
        setAccountName(settings.getString("ACCOUNT_NAME", null));
    }

    public boolean isSignedIn()
    {
        return Account.getSelectedAccountName() != null;
    }

    // setAccountName definition
    private void setAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ACCOUNT_NAME", accountName);
        editor.commit();
        Account.setSelectedAccountName(accountName);
    }

    public void chooseAccount(Activity activity) {
        if (!isSignedIn())
            activity.startActivityForResult(Account.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);
    }

    public void signIn(String accountName) {
        if (accountName != null) {
            setAccountName(accountName);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("ACCOUNT_NAME", accountName);
            editor.commit();
        }
    }
}
