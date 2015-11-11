package com.udacity.adcs.app.goodintents.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.squareup.picasso.Picasso;
import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.content.layout.PersonEventsColumns;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.objects.PersonEvent;
import com.udacity.adcs.app.goodintents.objects.PersonMedia;
import com.udacity.adcs.app.goodintents.ui.base.BaseActivity;
import com.udacity.adcs.app.goodintents.ui.list.FriendsListAdapter;
import com.udacity.adcs.app.goodintents.ui.list.PhotosListAdapter;
import com.udacity.adcs.app.goodintents.utils.Constants;
import com.udacity.adcs.app.goodintents.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by kyleparker on 11/9/2015.
 */
public class EventDetailActivity extends BaseActivity {

    static final int REQUEST_IMAGE_CAPTURE = 5;

    private Event mEvent = new Event();
    private PersonEvent mPersonEvent;
    private PersonMedia mPersonMedia;
    private long mEventId;

    private TextView mDesc;
    private TextView mOrg;
    private TextView mDate;
    private TextView mName;

    private ImageView mEventImage;

    private RecyclerView eventFriends;
    private RecyclerView eventPhotos;

    private FriendsListAdapter friendsListAdapter;
    private PhotosListAdapter photosListAdapter;

    private List<PersonEvent> mPersonList;
    private List<PersonMedia> mMediaList;

    private Boolean isChecked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mEventId = extras.getLong(Constants.Extra.EVENT_ID);
        }

        mPersonEvent = new PersonEvent();
        mPersonEvent = mProvider.getPersonEvent(mPerson.getId(), mEventId);

        if (mPersonEvent != null) {
            isChecked = true;
        }

        setupToolbar();
        setupView();
        getEvent();
        getMedia();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("EventDetailActivity", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == Constants.RequestCode.APP_INVITE) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO: Consider saving the AppInvite ids for notification/reporting purposes
                // Check how many invitations were sent and log a message The ids array contains the unique invitation
                // ids for each invitation sent (one for each contact select by the user). You can use these for analytics
                // as the ID will be consistent on the sending and receiving devices.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
//                Log.d(TAG, ids.toString());
            } else {
                Toast.makeText(mActivity, R.string.toast_error_inviting_friends, Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            addPhotoToDatabase();
            mMediaList = mProvider.getMediaByPersonEvent(mPerson.getId(), mEventId);
            photosListAdapter.addAll(mMediaList);
        }
    }

    private void getEvent() {

        Runnable load = new Runnable() {
            public void run() {
                try {
                    mEvent = mProvider.getEvent(mEventId);
                    mPersonList = mProvider.getPersonListByEvent(mEventId, Constants.Type.FRIEND);
                    if (mPersonList != null && !mPersonList.isEmpty()) {
                        findViewById(R.id.friends_header).setVisibility(View.VISIBLE);
                        friendsListAdapter.addAll(mPersonList);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mActivity.runOnUiThread(getEventRunnable);
                }
            }
        };

        Thread thread = new Thread(null, load, "getEvent");
        thread.start();
    }


    private final Runnable getEventRunnable = new Runnable() {
        public void run() {

            Picasso.with(getApplicationContext()).load(mEvent.getPhotoUrl()).into(mEventImage);

            mName.setText(mEvent.getName());
            mDesc.setText(mEvent.getDescription());
            mOrg.setText(mEvent.getOrganization());

            String mDateString = StringUtils.getDateString(mEvent.getDate(), Constants.DATE_FORMAT);
            mDate.setText(mDateString);

            final View coordinatorLayoutView = findViewById(R.id.coordinator_layout);

            final FloatingActionButton checkIn = (FloatingActionButton) findViewById(R.id.fab_checkin);

            if (isChecked) {
                checkIn.setImageResource(R.drawable.ic_fab_checkin_on);
            }

            checkIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isChecked) {
                        mPersonEvent = new PersonEvent();
                        mPersonEvent.setDate(System.currentTimeMillis());
                        mPersonEvent.setEventId(mEventId);
                        mPersonEvent.setPersonId(mPerson.getId());
                        mPersonEvent.setPoints(15);
                        mProvider.insertPersonEvent(mPersonEvent);
                        Snackbar
                                .make(coordinatorLayoutView, R.string.snackbar_event_checkin, Snackbar.LENGTH_SHORT)
                                .show();
                        checkIn.setImageResource(R.drawable.ic_fab_checkin_on);
                        isChecked = true;
                    } else {
                        mProvider.deletePersonEvent(mPerson.getId(), mEventId);
                        Snackbar
                                .make(coordinatorLayoutView, R.string.snackbar_event_checkout, Snackbar.LENGTH_SHORT)
                                .show();
                        checkIn.setImageResource(R.drawable.ic_fab_checkin_off);
                        isChecked = false;
                    }

                }
            });


        }
    };

    private void getMedia() {

        Runnable load = new Runnable() {
            public void run() {
                try {
                    mMediaList = mProvider.getMediaByPersonEvent(mPerson.getId(), mEventId);

                    if (mMediaList != null && !mMediaList.isEmpty()) {
                        findViewById(R.id.photos_header).setVisibility(View.VISIBLE);
                        photosListAdapter.addAll(mMediaList);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                }
            }
        };

        Thread thread = new Thread(null, load, "getMedia");
        thread.start();
    }

    /**
     * Setup the toolbar for the activity
     */
    private void setupToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
        toolbar.inflateMenu(R.menu.event_details);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_friend_invite:
                        startActivityForResult(getAppInviteJoinEventIntent(), Constants.RequestCode.APP_INVITE);
                        return true;
                    case R.id.menu_add_photo:
                        dispatchTakePictureIntent();
                        return true;
                }
                return false;
            }
        });
    }


    private void setupView() {

        mEventImage = (ImageView) findViewById(R.id.event_image);
        mDesc = (TextView) findViewById(R.id.event_description);
        mOrg = (TextView) findViewById(R.id.event_organization);
        mDate = (TextView) findViewById(R.id.event_date);
        mName = (TextView) findViewById(R.id.event_name);

        LinearLayoutManager friendsLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager photosLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        eventFriends = (RecyclerView) findViewById(R.id.friends_recycler_view);
        eventFriends.setLayoutManager(friendsLayoutManager);
        friendsListAdapter = new FriendsListAdapter(getApplicationContext());
        eventFriends.setAdapter(friendsListAdapter);


        eventPhotos = (RecyclerView) findViewById(R.id.photos_recycler_view);
        eventPhotos.setLayoutManager(photosLayoutManager);
        photosListAdapter = new PhotosListAdapter(getApplicationContext());
        eventPhotos.setAdapter(photosListAdapter);

    }

    /**
     * Create the intent to post the event invitation*
     * @return
     */
    private Intent getAppInviteJoinEventIntent() {
        // Launching the AppInviteInvitation intent opens the contact chooser where the user selects the contacts to
        // invite. Invites are sent via email or SMS.
        String callToActionDeepLinkId = Constants.APP_INVITE_HOST + Constants.APP_INVITE_DEEP_LINK_JOIN_EVENT +
                PersonEventsColumns.PERSON_ID + "=" + mPerson.getId() + "&" +
                PersonEventsColumns.EVENT_ID + "=" + mEventId;
        Uri callToActionUrl = Uri.parse(callToActionDeepLinkId);
        Uri customImage = Uri.parse(Constants.APP_INVITE_IMAGE_URL);
        String message = this.getString(R.string.content_value_invitation_message, mEvent.getName());

        if (message.length() > 100) {
            message = message.substring(0, 97) + "...";
        }

        return new AppInviteInvitation.IntentBuilder(this.getString(R.string.content_join_event_invite_title, mEvent.getName()))
                .setMessage(message)
                .setDeepLink(callToActionUrl)
                .setCustomImage(customImage)
                .setCallToActionText(this.getString(R.string.content_invite_friends))
                .build();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("Camera Error", ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + mPersonEvent.getId() + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void addPhotoToDatabase() {
        mPersonMedia = new PersonMedia();
        mPersonMedia.setMediaName("Photo");
        mPersonMedia.setPersonEventId(mPersonEvent.getId());
        mPersonMedia.setLocalStorageURL(mCurrentPhotoPath);
        mProvider.insertPersonMedia(mPersonMedia);
    }

}
