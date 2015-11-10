package com.udacity.adcs.app.goodintents.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.objects.PersonEvent;
import com.udacity.adcs.app.goodintents.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedram on 09/11/15.
 */
public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private List<PersonEvent> mListItems;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mEventNameTextView;
        TextView mEventDateTextView;
        TextView mOrganizationTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mEventNameTextView = (TextView) itemView.findViewById(R.id.event_name_textview);
            mEventDateTextView = (TextView) itemView.findViewById(R.id.event_date_textview);
            mOrganizationTextView = (TextView) itemView.findViewById(R.id.organization_text_view);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView mProfilePicImageView;
        TextView mNameTextView;
        TextView mPoints;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mProfilePicImageView = (ImageView) itemView.findViewById(R.id.profile_picture_imageview);
            mNameTextView = (TextView) itemView.findViewById(R.id.name_textview);
            mPoints = (TextView) itemView.findViewById(R.id.points_textview);
        }
    }


    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public EventListAdapter(List<PersonEvent> listItems) {
        this.mListItems = listItems;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_profile_header, parent, false);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(view);
            return headerViewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_HEADER) {
            // TODO update image later
            //((HeaderViewHolder) viewHolder).mProfilePicImageView.setImageResource(R.mipmap.ic_launcher);
            ((HeaderViewHolder) viewHolder).mNameTextView.setText("Pedram Veisi");
            ((HeaderViewHolder) viewHolder).mPoints.setText("Points: 100");

        } else {
            // Position - 1 counts for header
            PersonEvent personEvent = mListItems.get(position - 1);

            ((ItemViewHolder) viewHolder).mEventNameTextView.setText(personEvent.event.getName());
            // TODO Convert date to a human readable string
            ((ItemViewHolder) viewHolder).mEventDateTextView.setText(Long.toString(personEvent.getDate()));
            ((ItemViewHolder) viewHolder).mOrganizationTextView.setText(personEvent.event.getOrganization());
        }
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mListItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    public void setEventList(List<PersonEvent> eventList){
        mListItems = eventList;
    }
}