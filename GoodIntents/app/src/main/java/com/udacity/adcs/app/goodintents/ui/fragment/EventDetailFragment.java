package com.udacity.adcs.app.goodintents.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.content.AppProviderUtils;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.ui.base.BaseFragment;

/**
 * Created by kyleparker on 11/9/2015.
 */
public class EventDetailFragment extends BaseFragment {

    private Event mEvent = new Event();
    private long mEventId;
    private AppProviderUtils mProvider;

    private TextView mDesc;
    private TextView mOrg;
    private TextView mDate;

    private RecyclerView eventFriends;
    private RecyclerView eventPhotos;

    /**
     * Factory method to generate a new instance of the fragment
     *
     * @return
     */
    public static EventDetailFragment newInstance() {
        final EventDetailFragment f = new EventDetailFragment();

//        final Bundle args = new Bundle();
//        args.putInt(Constants.Extra.INTRO_TYPE, introType);
//
//        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_event_detail, container, false);
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mProvider = AppProviderUtils.Factory.get(mActivity);

        setupView();

        getEvent();


        return mRootView;


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


        }
    };

    private void setupView() {
        // TODO: Change date to date/time picker
        mDesc = (TextView) mRootView.findViewById(R.id.event_description);
        mOrg = (TextView) mRootView.findViewById(R.id.event_organization);
        mDate = (TextView) mRootView.findViewById(R.id.event_date);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        eventFriends = (RecyclerView) mRootView.findViewById(R.id.friends_recycler_view);
        eventPhotos = (RecyclerView) mRootView.findViewById(R.id.photos_recycler_view);

        eventFriends.setLayoutManager(layoutManager);
        eventPhotos.setLayoutManager(layoutManager);

        final FloatingActionButton checkIn = (FloatingActionButton) mRootView.findViewById(R.id.fab_checkin);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: onClick insert into person event for self.

                checkIn.setImageResource(R.drawable.ic_fab_checkin_on);

            }
        });
    }


}
