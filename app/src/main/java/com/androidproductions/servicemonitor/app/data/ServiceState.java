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
}
