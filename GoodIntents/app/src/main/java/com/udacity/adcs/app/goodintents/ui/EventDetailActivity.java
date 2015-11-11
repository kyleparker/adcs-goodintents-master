package com.udacity.adcs.app.goodintents.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.adcs.app.goodintents.R;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
            collapsingToolbarLayout.setTitle(mEvent.getName());

        }
    };

    /**
     * Setup the toolbar for the activity
     */
    private void setupToolbar() {
//        final Toolbar toolbar = getActionBarToolbar();
//        toolbar.setNavigationIcon(R.drawable.ic_action_up);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mActivity.finish();
//            }
//        });
    }


    private void setupView() {

        mEventImage = (ImageView) findViewById(R.id.event_image);
        mDesc = (TextView) findViewById(R.id.event_description);
        mOrg = (TextView) findViewById(R.id.event_organization);
        mDate = (TextView) findViewById(R.id.event_date);

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
                    mPersonEvent.setPoints(10);
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
}
