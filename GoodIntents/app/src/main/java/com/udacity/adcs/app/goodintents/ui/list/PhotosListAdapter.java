package com.udacity.adcs.app.goodintents.ui.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.PersonMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjaminshockley on 11/10/15.
 */
public class PhotosListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PersonMedia> mListItems;
    private Context mContext;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView mPhotoImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.list_item_event_photo);
        }
    }


    /**
     * Constructor
     *
     */
    public PhotosListAdapter(Context context) {
        this.mListItems = new ArrayList<>();
        this.mContext = context;
    }

    public void addAll(List<PersonMedia> media) {
        mListItems.clear();
        mListItems.addAll(media);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event_photo, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        PersonMedia mPersonMedia = mListItems.get(position);

        ImageView imageView = ((ItemViewHolder) viewHolder).mPhotoImageView;
        //Picasso.with(mContext).load(mPersonMedia.getLocalStorageURL()).into(imageView);
        setPic(imageView, mPersonMedia);


    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public void setEventList(ArrayList<PersonMedia> eventList){
        mListItems = eventList;
    }


    private void setPic(ImageView imageView, PersonMedia mPersonMedia) {
        // Get the dimensions of the View
        int targetW = 120;
        int targetH = 120;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPersonMedia.getLocalStorageURL().substring(5), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mPersonMedia.getLocalStorageURL().substring(5), bmOptions);
        imageView.setImageBitmap(bitmap);
    }
}
