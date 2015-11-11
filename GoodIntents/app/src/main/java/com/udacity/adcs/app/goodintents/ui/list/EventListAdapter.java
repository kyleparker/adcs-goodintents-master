package com.udacity.adcs.app.goodintents.ui.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.Person;
import com.udacity.adcs.app.goodintents.objects.PersonEvent;
import com.udacity.adcs.app.goodintents.ui.EventDetailActivity;
import com.udacity.adcs.app.goodintents.utils.Constants;

import java.util.List;

/**
 * Created by pedram on 09/11/15.
 */
public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private final Context mContext;

    private List<PersonEvent> mListItems;
    private Person mPerson;

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mEventNameTextView;
        private TextView mEventDateTextView;
        private TextView mOrganizationTextView;
        private TextView mEventPoints;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mEventNameTextView = (TextView) itemView.findViewById(R.id.event_name_textview);
            mEventDateTextView = (TextView) itemView.findViewById(R.id.event_date_textview);
            mOrganizationTextView = (TextView) itemView.findViewById(R.id.organization_text_view);
            mEventPoints = (TextView) itemView.findViewById(R.id.event_points_textview);

            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            // TODO Launch event detail activity
            Intent intent = new Intent(mContext, EventDetailActivity.class);
            intent.putExtra(Constants.Extra.EVENT_ID, mListItems.get(getPosition()- 1).getEventId());
            mContext.startActivity(intent);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView mProfilePicImageView;
        TextView mNameTextView;
        TextView mPersonPoints;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mProfilePicImageView = (ImageView) itemView.findViewById(R.id.profile_picture_imageview);
            mNameTextView = (TextView) itemView.findViewById(R.id.name_textview);
            mPersonPoints = (TextView) itemView.findViewById(R.id.person_points_textview);
        }
    }


    /**
     * Constructor
     *
     * @param context  The current context.
     * he resource ID for a layout file containing a TextView to use when
     */
    public EventListAdapter(Context context, List<PersonEvent> listItems) {
        this.mContext = context;
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
            if(mPerson != null){
                // TODO update image later
                //((HeaderViewHolder) viewHolder).mProfilePicImageView.setImageResource(R.mipmap.ic_launcher);
                ((HeaderViewHolder) viewHolder).mNameTextView.setText(mPerson.getDisplayName());
                ((HeaderViewHolder) viewHolder).mPersonPoints.setText("Points: 100");
            }

        } else {
            if (mListItems.size() != 0){
                // Position - 1 counts for header
                PersonEvent personEvent = mListItems.get(position - 1);

                ((ItemViewHolder) viewHolder).mEventNameTextView.setText(personEvent.event.getName());
                // TODO Convert date to a human readable string
                ((ItemViewHolder) viewHolder).mEventDateTextView.setText(Long.toString(personEvent.getDate()));
                ((ItemViewHolder) viewHolder).mOrganizationTextView.setText(personEvent.event.getOrganization());
                ((ItemViewHolder) viewHolder).mEventPoints.setText(String.valueOf(personEvent.getPoints()));
            }
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

    public void setProfileData(Person person) {
        this.mPerson = person;
    }
}