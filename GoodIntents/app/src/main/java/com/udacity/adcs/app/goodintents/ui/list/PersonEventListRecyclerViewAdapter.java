package com.udacity.adcs.app.goodintents.ui.list;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.PersonEvent;
import com.udacity.adcs.app.goodintents.utils.PicassoRoundTransform;
import com.udacity.adcs.app.goodintents.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by perklun on 11/10/2015.
 */
public class PersonEventListRecyclerViewAdapter extends RecyclerView.Adapter<PersonEventListRecyclerViewAdapter.ViewHolder> {

    private List<PersonEvent> mDataset;
    private static Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_friend_name;
//        public TextView tv_friend_event_date;

//        private ImageView mProfilePicImageView;
        private TextView mEventNameTextView;
        private TextView mEventDateTextView;
        private TextView mOrganizationTextView;
        private TextView mEventPoints;
        private ImageView mEventThumbnail;
        private LinearLayout mPointsContainer;

        //        public TextView tv_friend_event_details;
//        public ImageView iv_friend_event_image;
//        public ImageView iv_friend_pic;
        public ViewHolder(View v) {
            super(v);
            tv_friend_name = (TextView) v.findViewById(R.id.friend_name_text_view);
//            tv_friend_event_date = (TextView) v.findViewById(R.id.tv_friend_event_date);

//            mProfilePicImageView = (ImageView) itemView.findViewById(R.id.iv_friend_pic);
            mEventNameTextView = (TextView) itemView.findViewById(R.id.event_name_textview);
            mEventDateTextView = (TextView) itemView.findViewById(R.id.event_date_textview);
            mOrganizationTextView = (TextView) itemView.findViewById(R.id.organization_text_view);
            mEventPoints = (TextView) itemView.findViewById(R.id.event_points_text_view);
            mEventThumbnail = (ImageView) itemView.findViewById(R.id.event_thumbnail_imageview);
            mPointsContainer = (LinearLayout) itemView.findViewById(R.id.points_linear_layout);

//            tv_friend_event_details = (TextView) v.findViewById(R.id.tv_friend_event_details);
//            iv_friend_event_image = (ImageView) v.findViewById(R.id.iv_friend_event_image);
//            iv_friend_pic = (ImageView) v.findViewById(R.id.iv_friend_pic);
        }
    }

    public PersonEventListRecyclerViewAdapter(Context context, List<PersonEvent> mDataset) {
        this.mDataset = mDataset;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event_with_picture, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PersonEvent e = mDataset.get(position);

        if (e != null) {
            holder.tv_friend_name.setText(e.person.getDisplayName());
//            holder.tv_friend_event_date.setText(StringUtils.getDateString(e.event.getDate(), "MMM dd, yyyy hh:mm a"));
//          holder.tv_friend_event_details.setText(e.event.getDescription());
//          String photo_url = e.event.getPhotoUrl();
//        if(photo_url != null & photo_url.length() > 0){
//            Picasso.with(mContext)
//                    .load(photo_url)
//                    .into( holder.iv_friend_event_image);
//        }

//            holder.mPointsContainer.setVisibility(View.VISIBLE);
            //May want to try and catch this
            String mName = e.person.getDisplayName();
            mName = mName.replaceAll(" ", "_").toLowerCase();
            Uri iconUri = Uri.parse("android.resource://" + mContext.getPackageName() +
                    "/drawable/" + mName);
//        holder.iv_friend_pic.setImageURI(iconUri);
//            Picasso.with(mContext).load(iconUri).
//                    transform(new PicassoRoundTransform()).into(holder.mProfilePicImageView);

            holder.mEventNameTextView.setText(e.event.getName());

            Date date = new java.util.Date(e.event.getDate() * 1000);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            String month = new SimpleDateFormat("MMM").format(calendar.getTime());
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            holder.mEventDateTextView.setText(month + " " + day);

            holder.mOrganizationTextView.setText("Participated in");

            if (!TextUtils.isEmpty(e.event.getPhotoUrl())) {
                Picasso.with(mContext).load(e.event.getPhotoUrl()).
                        transform(new PicassoRoundTransform()).into(holder.mEventThumbnail);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setEventList(List<PersonEvent> mdataset) {
        this.mDataset = mdataset;
    }
}
