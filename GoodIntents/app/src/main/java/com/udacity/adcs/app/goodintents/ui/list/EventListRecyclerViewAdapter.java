package com.udacity.adcs.app.goodintents.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.utils.StringUtils;

import java.util.List;

/**
 * Created by perklun on 11/10/2015.
 */
public class EventListRecyclerViewAdapter extends RecyclerView.Adapter<EventListRecyclerViewAdapter.ViewHolder> {

    private List<Event> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        }
    }

    public EventListRecyclerViewAdapter(List<Event> mDataset){
        this.mDataset = mDataset;
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
        Event e = mDataset.get(position);
        holder.tv_friend_name.setText(e.getOrganization());
        holder.tv_friend_event_details.setText(e.getDescription());
        holder.tv_friend_event_date.setText(StringUtils.getRelativeTimeAgo(e.getDate()));
        holder.iv_friend_pic.setImageResource(R.mipmap.ic_launcher);
        holder.iv_friend_event_image.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setEventList(List<Event> mdataset){
        this.mDataset = mdataset;
    }
}
