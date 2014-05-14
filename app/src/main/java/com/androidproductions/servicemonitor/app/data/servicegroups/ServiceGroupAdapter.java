package com.androidproductions.servicemonitor.app.data.servicegroups;

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
import com.androidproductions.servicemonitor.app.data.services.ServiceState;
import com.androidproductions.servicemonitor.app.data.services.ServiceStatusContract;

import java.util.Date;

public class ServiceGroupAdapter extends CursorAdapter {

    public ServiceGroupAdapter(final Context context) {
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
                        (TextView)ret.findViewById(R.id.service_name)
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
                cursor.getColumnIndexOrThrow(ServiceGroupContract.NAME));
        vh.Name.setText(name);
        return ret;
    }


    private class ViewHolder {
        TextView Name;
        private ViewHolder(TextView name) {
            Name = name;
        }
    }
}
