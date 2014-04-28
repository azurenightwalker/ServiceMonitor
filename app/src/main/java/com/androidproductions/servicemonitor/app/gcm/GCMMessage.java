package com.androidproductions.servicemonitor.app.gcm;

import android.content.Context;
import android.os.Bundle;

import com.androidproductions.generic.lib.dates.DateFormatter;
import com.androidproductions.servicemonitor.app.R;
import com.androidproductions.servicemonitor.app.data.ServiceState;

import java.util.Date;
import java.util.Locale;

public class GCMMessage {

    final String serviceId;
    final String serviceGroup;
    final int status;
    final long time;
    private final Context _context;

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public int getStatus() {
        return status;
    }

    public long getLastUpdate() {
        return time;
    }

    public GCMMessage(Bundle extras, Context context) {
        serviceId = extras.getString("serviceId");
        serviceGroup = extras.getString("serviceGroup");
        status = Integer.parseInt(extras.getString("status"));
        time = Long.parseLong(extras.getString("time"));
        _context = context;
    }

    @Override
    public String toString() {
        String formatString = _context.getString(R.string.statusFormatString);
        switch (ServiceState.parse(status))
        {
            case DOWN:
                return String.format(Locale.getDefault(), formatString,
                        serviceId, _context.getString(R.string.DOWN),
                        DateFormatter.AsCuiDateTime(new Date(time)));
            case OK:
                return String.format(Locale.getDefault(), formatString,
                        serviceId, _context.getString(R.string.BACK_UP),
                        DateFormatter.AsCuiDateTime(new Date(time)));
            case INACTIVE:
                return String.format(Locale.getDefault(), formatString,
                        serviceId, _context.getString(R.string.CURRENTLY_INACTIVE),
                        DateFormatter.AsCuiDateTime(new Date(time)));
        }
        return super.toString();
    }
}
