package com.udacity.adcs.app.goodintents.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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

import java.util.List;

/**
 *
 * Created by kyleparker on 11/9/2015.
 */
public class EventDetailActivity extends BaseActivity {

    private Event mEvent = new Event();
    private PersonEvent mPersonEvent;
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

        if (mProvider.getPersonEvent(mPerson.getId(), mEventId) != null) {
            isChecked = true;
        }

        setupToolbar();
        setupView();
        getEvent();
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
    }

    private void getEvent() {

        Runnable load = new Runnable() {
            public void run() {
                try {
                    mEvent = mProvider.getEvent(mEventId);
                    mPersonList = mProvider.getPersonListByEvent(mEventId, Constants.Type.FRIEND);
                    mMediaList = mProvider.getMediaByPersonEvent(Constants.Type.SELF, mEventId);
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

            if (mPersonList != null && !mPersonList.isEmpty()) {
                findViewById(R.id.friends_header).setVisibility(View.VISIBLE);
                friendsListAdapter.addAll(mPersonList);
            }

            if (mMediaList != null && !mMediaList.isEmpty()) {
                findViewById(R.id.photos_header).setVisibility(View.VISIBLE);
                photosListAdapter.addAll(mMediaList);
            }

            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//            collapsingToolbarLayout.setTitle(mEvent.getName());

        }
    };

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
                        //TODO: Launch camera.
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

        final View coordinatorLayoutView = findViewById(R.id.coordinator_layout);

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

    /**
     * Create the intent to post the event invitation*
     * @return
     */
    private Intent getAppInviteJoinEventIntent() {
        // Launching the AppInviteInvitation intent opens the contact chooser where the user selects the contacts to
        // invite. Invites are sent via email or SMS.
        String callToActionDeepLinkId = Constants.APP_INVITE_HOST + Constants.APP_INVITE_DEEP_LINK_JOIN_EVENT +
                PersonEventsColumns.PERSON_ID + "=" + mPersonEvent.getPersonId() + "&" +
                PersonEventsColumns.EVENT_ID + "=" + mPersonEvent.getEventId();
        Uri callToActionUrl = Uri.parse(callToActionDeepLinkId);
        Uri customImage = Uri.parse(Constants.APP_INVITE_IMAGE_URL);
        String message = this.getString(R.string.content_value_invitation_message, mPersonEvent.event.getName());

        if (message.length() > 100) {
            message.substring(0, 100);
        }

        return new AppInviteInvitation.IntentBuilder(this.getString(R.string.content_join_event_invite_title, mPersonEvent.event.getName()))
                .setMessage(message)
                .setDeepLink(callToActionUrl)
                .setCustomImage(customImage)
                .setCallToActionText(this.getString(R.string.content_invite_friends))
                .build();
    }

}
