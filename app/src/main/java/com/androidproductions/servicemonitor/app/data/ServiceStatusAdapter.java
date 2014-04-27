package com.androidproductions.servicemonitor.app.data;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidproductions.generic.lib.dates.DateFormatter;
import com.androidproductions.servicemonitor.app.R;
import com.androidproductions.servicemonitor.app.fragments.ServiceDetailsFragment;

import java.util.Date;

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
            ret.setTag(
                    new ViewHolder(
                            (TextView)ret.findViewById(R.id.service_name),
                            (TextView)ret.findViewById(R.id.service_update),
                            (ImageView)ret.findViewById(R.id.service_status)
                    ));
            return populateView(cursor, ret);
        }
        return null;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        populateView(cursor,view);
    }

    private View populateView(Cursor cursor, View ret) {
        ViewHolder vh = (ViewHolder) ret.getTag();

        final String name = cursor.getString(
                cursor.getColumnIndexOrThrow(ServiceStatusContract.NAME));
        final Date lastUpdate = new Date(cursor.getLong(
                cursor.getColumnIndexOrThrow(ServiceStatusContract.LAST_UPDATE)));
        final ServiceState status = ServiceState.parse(cursor.getInt(
                cursor.getColumnIndexOrThrow(ServiceStatusContract.STATUS)));

        vh.Name.setText(name);
        vh.LastUpdate.setText(DateFormatter.AsCuiDateTime(lastUpdate));

        ((ImageView) vh.Status.findViewById(R.id.service_status))
                .setImageResource(status.getResource());
        return ret;
    }


    private class ViewHolder {
        TextView Name;
        TextView LastUpdate;
        ImageView Status;

        private ViewHolder(TextView name, TextView lastUpdate, ImageView status) {
            Name = name;
            LastUpdate = lastUpdate;
            Status = status;
        }
    }
}
