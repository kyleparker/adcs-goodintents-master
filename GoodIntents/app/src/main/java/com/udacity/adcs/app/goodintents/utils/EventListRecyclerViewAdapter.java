package com.udacity.adcs.app.goodintents.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.adcs.app.goodintents.R;

/**
 * Created by perklun on 11/9/2015.
 */
public class EventListRecyclerViewAdapter extends RecyclerView.Adapter<EventListRecyclerViewAdapter.ViewHolder> {

    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_event_title;
        public TextView tv_event_date;
        public TextView tv_event_details;
        public ImageView iv_event_image;
        public ViewHolder(View v) {
            super(v);
            tv_event_date = (TextView) v.findViewById(R.id.tv_event_date);
            tv_event_details = (TextView) v.findViewById(R.id.tv_event_details);
            tv_event_title = (TextView) v.findViewById(R.id.tv_event_title);
            iv_event_image = (ImageView) v.findViewById(R.id.iv_event_image);
        }
    }

    public EventListRecyclerViewAdapter(String[] mDataset){
        this.mDataset = mDataset;
    }

    @Override
    public EventListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(EventListRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.tv_event_date.setText("11/11");
        holder.tv_event_details.setText("Fun run");
        holder.tv_event_title.setText("Awesome event");
        holder.iv_event_image.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
