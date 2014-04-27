package com.androidproductions.servicemonitor.app.data;

public enum ServiceState {
    OK(1),
    INACTIVE(1<<1),
    DOWN(1<<2);

    public final int Value;

    ServiceState(final int val) {
        this.Value = val;
    }

    public static ServiceState parse(final int val)
    {
        switch (val)
        {
            case 1:
                return ServiceState.OK;
            case 1<<1:
                return ServiceState.INACTIVE;
            case 1<<2:
                return ServiceState.DOWN;
            default:
                return null;
        }
    }

    public int getResource() {
        int res = android.R.drawable.presence_invisible;
        switch(this)
        {
            case OK:
                res = android.R.drawable.presence_online;
                break;
            case INACTIVE:
                res = android.R.drawable.presence_away;
                break;
            case DOWN:
                res = android.R.drawable.presence_busy;
                break;
        }
        return res;
    }
}
