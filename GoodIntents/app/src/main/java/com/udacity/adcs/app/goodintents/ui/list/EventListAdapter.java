package com.udacity.adcs.app.goodintents.ui.list;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pedram on 09/11/15.
 */
public class EventListAdapter extends ArrayAdapter<String> {

    private Context mContext;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public EventListAdapter(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    /**
     * View holder for activity list adapter
     */
    static class ViewHolder {
        TextView eventNameTextView;
        TextView eventDateTextView;
        TextView pointsTextView;
        ImageView pointsIconImageView;
    }

}