package com.udacity.adcs.app.goodintents.ui.list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.Person;
import com.udacity.adcs.app.goodintents.objects.PersonEvent;
import com.udacity.adcs.app.goodintents.ui.EventDetailActivity;
import com.udacity.adcs.app.goodintents.utils.Constants;
import com.udacity.adcs.app.goodintents.utils.PicassoRoundTransform;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private int mPersonTotalPoints;

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mEventNameTextView;
        private TextView mEventDateTextView;
        private TextView mOrganizationTextView;
        private TextView mEventPoints;
        private ImageView mEventThumbnail;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mEventNameTextView = (TextView) itemView.findViewById(R.id.event_name_textview);
            mEventDateTextView = (TextView) itemView.findViewById(R.id.event_date_textview);
            mOrganizationTextView = (TextView) itemView.findViewById(R.id.organization_text_view);
            mEventPoints = (TextView) itemView.findViewById(R.id.event_points_text_view);
            mEventThumbnail = (ImageView) itemView.findViewById(R.id.event_thumbnail_imageview);

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

                String displayName = mPerson.getDisplayName();
                displayName = displayName.replaceAll(" ", "_").toLowerCase();

                Uri profilePicUri = Uri.parse("android.resource://" + mContext.getPackageName() +
                        "/drawable/" + displayName);

                ((HeaderViewHolder) viewHolder).mProfilePicImageView.setImageURI(profilePicUri);
                ((HeaderViewHolder) viewHolder).mNameTextView.setText(mPerson.getDisplayName());
                ((HeaderViewHolder) viewHolder).mPersonPoints.setText(Integer.toString(mPersonTotalPoints));
            }

        } else {
            if (mListItems.size() != 0){
                // Position - 1 counts for header
                PersonEvent personEvent = mListItems.get(position - 1);

                ((ItemViewHolder) viewHolder).mEventNameTextView.setText(personEvent.event.getName());

                Date date = new java.util.Date(personEvent.event.getDate() * 1000);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                String month = new SimpleDateFormat("MMM").format(calendar.getTime());
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                ((ItemViewHolder) viewHolder).mEventDateTextView.setText(month + ", " + day);

                ((ItemViewHolder) viewHolder).mOrganizationTextView.setText(personEvent.event.getOrganization());
                ((ItemViewHolder) viewHolder).mEventPoints.setText(String.valueOf(personEvent.getPoints()));

                Picasso.with(mContext).load(personEvent.event.getPhotoUrl()).
                transform(new PicassoRoundTransform()).into(((ItemViewHolder) viewHolder).mEventThumbnail);

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

    public void setPersonTotalPoints(int personTotalPoints) {
        this.mPersonTotalPoints = personTotalPoints;
    }
}