package com.udacity.adcs.app.goodintents.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.content.AppProviderUtils;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.objects.PersonEvent;
import com.udacity.adcs.app.goodintents.ui.base.BaseFragment;
import com.udacity.adcs.app.goodintents.ui.list.EventListAdapter;
import com.udacity.adcs.app.goodintents.ui.list.EventListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyleparker on 11/9/2015.
 */
public class EventListFragment extends BaseFragment {
    private List<Event> mEventList;
    private EventListRecyclerViewAdapter mEventListRecyclerViewAdapter;
    /**
     * Factory method to generate a new instance of the fragment
     *
     * @return
     */
    public static EventListFragment newInstance() {
        final EventListFragment f = new EventListFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_event_list, container, false);
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //Add recycler view to fragment
        RecyclerView rv_event_list = (RecyclerView) mRootView.findViewById(R.id.rv_event_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        rv_event_list.setLayoutManager(mLayoutManager);
        mEventListRecyclerViewAdapter = new EventListRecyclerViewAdapter(new ArrayList<Event>());
        getEventList();
        rv_event_list.setAdapter(mEventListRecyclerViewAdapter);
        return mRootView;
    }

    public void getEventList() {
        Runnable load = new Runnable() {
            public void run() {
                try {
                    mEventList = mProvider.getEventList();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mActivity.runOnUiThread(getEventListRunnable);
                }
            }
        };
        Thread thread = new Thread(null, load, "getEventList");
        thread.start();
    }

    private final Runnable getEventListRunnable = new Runnable() {
        public void run() {
            mEventListRecyclerViewAdapter.setEventList(mEventList);
            mEventListRecyclerViewAdapter.notifyDataSetChanged();
        }
    };
}