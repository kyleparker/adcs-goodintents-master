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
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.ui.EventDetailActivity;
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
    private final Context mContext;


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
            mMapImageView = (ImageView) itemView.findViewById(R.id.list_item_event_photo);
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
            ((MapViewHolder) holder).mMapImageView.setImageResource(R.mipmap.ic_launcher);
        } else{
            Event e = mDataset.get(position-1);
            ((ViewHolder)holder).tv_friend_name.setText(e.getOrganization());
            ((ViewHolder)holder).tv_friend_event_details.setText(e.getDescription());
            ((ViewHolder)holder).tv_friend_event_date.setText(StringUtils.getRelativeTimeAgo(e.getDate()));
            ((ViewHolder)holder).iv_friend_pic.setImageResource(R.mipmap.ic_launcher);
            ((ViewHolder)holder).iv_friend_event_image.setImageResource(R.mipmap.ic_launcher);
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
