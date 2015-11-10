package com.udacity.adcs.app.goodintents.ui.list;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.Event;

import java.util.ArrayList;

/**
 * Created by pedram on 09/11/15.
 */
public class EventListAdapter extends ArrayAdapter<Event> {

    private Context mContext;
    private ArrayList<Event> mListItems;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public EventListAdapter(Context context, int resource, ArrayList<Event> listItems) {
        super(context, resource, listItems);
        this.mContext = context;
        this.mListItems = listItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_event, parent, false);

            // Setup view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.eventNameTextView = (TextView) convertView.findViewById(R.id.event_name_textview);
            viewHolder.eventDateTextView = (TextView) convertView.findViewById(R.id.event_date_textview);
            viewHolder.organizationTextView = (TextView) convertView.findViewById(R.id.organization_text_view);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        Event event = mListItems.get(position);

        viewHolder.eventNameTextView.setText(event.getName());

        // TODO Convert date to a human readable string
        viewHolder.eventDateTextView.setText(Long.toString(event.getDate()));

        viewHolder.organizationTextView.setText(event.getOrganization());

        return convertView;
    }

    /**
     * View holder for activity list adapter
     */
    static class ViewHolder {
        TextView eventNameTextView;
        TextView eventDateTextView;
        TextView organizationTextView;
    }

}