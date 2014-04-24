package com.androidproductions.generic.lib.drawer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidproductions.generic.lib.R;

public class DrawerAdapter extends ArrayAdapter<MenuOption>{
    private final int layoutResourceId;
    private final Context context;
    private final MenuOption[] data;

    public DrawerAdapter(Context context, int layoutResourceId, MenuOption[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        OptionHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new OptionHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (OptionHolder)row.getTag();
        }

        MenuOption opt = data[position];
        holder.txtTitle.setText(opt.getTitle());

        //noinspection ResourceType
        holder.txtTitle.setCompoundDrawablesWithIntrinsicBounds(
                context.getResources().getDrawable(opt.getResource()),null,null,null
        );
        return row;
    }

    public String getName(int position)
    {
        //noinspection ResourceType
        return context.getResources().getString(data[position].getTitle());
    }

    public Fragment getFragment(int position) {
        return data[position].createFragment();
    }

    static class OptionHolder
    {
        TextView txtTitle;
    }
}
