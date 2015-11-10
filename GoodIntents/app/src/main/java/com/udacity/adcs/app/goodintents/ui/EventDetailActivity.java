package com.udacity.adcs.app.goodintents.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.content.AppProviderUtils;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.ui.base.BaseActivity;
import com.udacity.adcs.app.goodintents.utils.Constants;
import com.udacity.adcs.app.goodintents.utils.IntentUtils;
import com.udacity.adcs.app.goodintents.utils.StringUtils;

/**
 *
 * Created by kyleparker on 11/9/2015.
 */
public class EventDetailActivity extends BaseActivity {

    private Event mEvent = new Event();
    private long mEventId;
    private AppProviderUtils mProvider;

    private TextView mDesc;
    private TextView mOrg;
    private TextView mDate;

    private RecyclerView eventFriends;
    private RecyclerView eventPhotos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        getEvent();
        setupToolbar();
        setupView();
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
            // Do something wtih mEvent - load adapter, etc

            mDesc.setText(mEvent.getDescription());
            mOrg.setText(mEvent.getOrganization());

            String mDateString = StringUtils.getDateString(mEvent.getDate(), Constants.DATE_FORMAT);
            mDate.setText(mDateString);


        }
    };

    /**
     * Setup the toolbar for the activity
     */
    private void setupToolbar() {
        final Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_action_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivity(IntentUtils.newIntent(mActivity, FeedActivity.class));
            }
        });
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle(mEvent.getName());
            }
        });
    }

    private void setupView() {
        mDesc = (TextView) findViewById(R.id.event_description);
        mOrg = (TextView) findViewById(R.id.event_organization);
        mDate = (TextView) findViewById(R.id.event_date);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        eventFriends = (RecyclerView) findViewById(R.id.friends_recycler_view);
        eventPhotos = (RecyclerView) findViewById(R.id.photos_recycler_view);

        eventFriends.setLayoutManager(layoutManager);
        eventPhotos.setLayoutManager(layoutManager);

        final FloatingActionButton checkIn = (FloatingActionButton) findViewById(R.id.fab_checkin);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: onClick insert into PersonEvent table for self.

                checkIn.setImageResource(R.drawable.ic_fab_checkin_on);

            }
        });
    }
}
