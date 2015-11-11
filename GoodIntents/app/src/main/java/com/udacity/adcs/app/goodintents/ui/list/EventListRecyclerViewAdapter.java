package com.udacity.adcs.app.goodintents.ui.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.ui.EventDetailActivity;
import com.udacity.adcs.app.goodintents.ui.MapActivity;
import com.udacity.adcs.app.goodintents.utils.Constants;
import com.udacity.adcs.app.goodintents.utils.StringUtils;

import java.util.List;

/**
 * Created by perklun on 11/10/2015.
 */
public class EventListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private List<Event> mDataset;
    private static Context mContext;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_friend_name;
        public TextView tv_friend_event_details;
        public TextView tv_friend_event_date;
        public ImageView iv_friend_event_image;
        public ImageView iv_friend_pic;
        public ViewHolder(View v) {
            super(v);
            tv_friend_name = (TextView) v.findViewById(R.id.tv_friend_name);
            tv_friend_event_details = (TextView) v.findViewById(R.id.tv_friend_event_details);
            tv_friend_event_date = (TextView) v.findViewById(R.id.tv_friend_event_date);
            iv_friend_event_image = (ImageView) v.findViewById(R.id.iv_friend_event_image);
            iv_friend_pic = (ImageView) v.findViewById(R.id.iv_friend_pic);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, EventDetailActivity.class);
                    intent.putExtra(Constants.Extra.EVENT_ID, mDataset.get(getPosition() - 1).getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public static class MapViewHolder extends RecyclerView.ViewHolder {
        ImageView mMapImageView;
        public MapViewHolder(View itemView) {
            super(itemView);
            mMapImageView = (ImageView) itemView.findViewById(R.id.item_map);
            mMapImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, MapActivity.class));
                }
            });
        }
    }

    public EventListRecyclerViewAdapter(Context context, List<Event> mDataset){
        this.mContext = context;
        this.mDataset = mDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        if(viewType == VIEW_TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event_photo, parent, false);
            MapViewHolder mapViewHolder = new MapViewHolder(view);
            return mapViewHolder;
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_event_with_picture, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == VIEW_TYPE_HEADER){
            ((MapViewHolder) holder).mMapImageView.setImageResource(R.drawable.map);
        } else{
            Event e = mDataset.get(position-1);
            ((ViewHolder)holder).tv_friend_name.setText(e.getOrganization());
            ((ViewHolder)holder).tv_friend_event_details.setText(e.getDescription());
            ((ViewHolder)holder).tv_friend_event_date.setText(StringUtils.getDateString(e.getDate(), "MMM dd, yyyy hh:mm a"));
            String photo_url = e.getPhotoUrl();
            if(photo_url != null && photo_url.length() > 0){
                Picasso.with(mContext)
                        .load(photo_url)
                        .into(((ViewHolder) holder).iv_friend_event_image);
            }
            String organizer_photo_url = e.getOrgPhotoUrl();
            if(organizer_photo_url != null && organizer_photo_url.length() > 0){
                Picasso.with(mContext)
                        .load(organizer_photo_url)
                        .into(((ViewHolder)holder).iv_friend_pic);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    public void setEventList(List<Event> mdataset){
        this.mDataset = mdataset;
    }
}
