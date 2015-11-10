package com.udacity.adcs.app.goodintents.ui.list;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.PersonEvent;

import java.util.List;

/**
 * Created by benjaminshockley on 11/10/15.
 */
public class FriendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PersonEvent> mListItems;
    private Context mContext;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView mFriendImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mFriendImageView = (ImageView) itemView.findViewById(R.id.list_item_person);
        }
    }


    /**
     * Constructor
     *
     */
    public FriendsListAdapter(List<PersonEvent> listItems, Context context) {
        this.mListItems = listItems;
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_friend, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        PersonEvent mPersonEvent = mListItems.get(position);

        String mName = mPersonEvent.person.getDisplayName();
        mName.replace(" ", "_");

        Uri iconUri = Uri.parse("android.resource://" + mContext.getPackageName() +
                "/drawable/" + mName);

        ((ItemViewHolder) viewHolder).mFriendImageView.setImageURI(iconUri);

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

    public void setEventList(List<PersonEvent> eventList){
        mListItems = eventList;
    }
}
