package com.androidproductions.servicemonitor.app;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class Credentials {
    private static Credentials credentials;
    public static final int REQUEST_ACCOUNT_PICKER = 999;
    private final SharedPreferences settings;
    public final GoogleAccountCredential Account;
    private final Activity _context;

    public static Credentials Get(Activity context)
    {
        if (credentials == null)
            credentials = new Credentials(context);
        return credentials;
    }

    public static Credentials Get()
    {
        if (credentials == null)
            throw new IllegalStateException();
        return credentials;
    }
    
    public Credentials(Activity context)
    {
        _context = context;
        settings = _context.getSharedPreferences("GoogleAccount", 0);
        Account = GoogleAccountCredential.usingAudience(_context, "server:client_id:154710985820-grgcf905ln13tno9g93qlusu18i5ievc.apps.googleusercontent.com");
        setAccountName(settings.getString("ACCOUNT_NAME", null));
        if (!isSignedIn()) {
            chooseAccount();
        }
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

    void chooseAccount() {
        _context.startActivityForResult(Account.newChooseAccountIntent(),
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
