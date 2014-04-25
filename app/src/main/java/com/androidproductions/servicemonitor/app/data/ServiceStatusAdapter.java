package com.androidproductions.servicemonitor.app.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidproductions.servicemonitor.app.R;

public class ServiceStatusAdapter extends CursorAdapter {

    public ServiceStatusAdapter(final Context context) {
        super(context,null,0);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout ret = (RelativeLayout) inflater.inflate(R.layout.service_status_list_item, parent, false);
        if (ret != null)
        {
            return populateView(cursor, ret);
        }
        return null;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        populateView(cursor,view);
    }

    private View populateView(Cursor cursor, View ret) {
        final TextView mName = (TextView) ret.findViewById(R.id.service_name);
        final TextView mGroup = (TextView) ret.findViewById(R.id.service_group);
        final ImageView mStat = (ImageView) ret.findViewById(R.id.service_status);

        final int nameIdx = cursor.getColumnIndexOrThrow(ServiceStatusContract.NAME);
        final int desc = cursor.getColumnIndex(ServiceStatusContract.GROUP);
        final int stat = cursor.getColumnIndex(ServiceStatusContract.STATUS);
        final String name = cursor.getString(nameIdx);
        final String description = cursor.getString(desc);
        final ServiceState status = ServiceState.parse(cursor.getInt(stat));

        mName.setText(name);
        mGroup.setText(description);

        int res = android.R.drawable.presence_invisible;
        switch(status)
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
        mStat.setImageResource(res);
        return ret;
    }


}
