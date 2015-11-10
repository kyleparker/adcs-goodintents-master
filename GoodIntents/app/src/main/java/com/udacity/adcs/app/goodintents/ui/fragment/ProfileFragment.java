package com.udacity.adcs.app.goodintents.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.content.AppContentProvider;
import com.udacity.adcs.app.goodintents.content.AppProviderUtils;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.objects.Person;
import com.udacity.adcs.app.goodintents.objects.PersonEvent;
import com.udacity.adcs.app.goodintents.ui.base.BaseFragment;
import com.udacity.adcs.app.goodintents.ui.list.EventListAdapter;
import com.udacity.adcs.app.goodintents.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyleparker on 11/9/2015.
 */
public class ProfileFragment extends BaseFragment {

    private List<PersonEvent> mEventList;
    private AppProviderUtils mProvider;
    private EventListAdapter mEventListAdapter;
    private Person mPerson;

    /**
     * Factory method to generate a new instance of the fragment
     *
     * @return
     */
    public static ProfileFragment newInstance() {
        final ProfileFragment f = new ProfileFragment();

//        final Bundle args = new Bundle();
//        args.putInt(Constants.Extra.INTRO_TYPE, introType);
//
//        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mProvider = new AppProviderUtils(getActivity().getContentResolver());
        mEventList = new ArrayList();
        //getPersonObject();
        getEventList();

        RecyclerView eventsRecyclerView = (RecyclerView) mRootView.findViewById(R.id.events_recycler_view);
        mEventListAdapter = new EventListAdapter(getActivity(), mEventList);
        eventsRecyclerView.setAdapter(mEventListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        eventsRecyclerView.setLayoutManager(linearLayoutManager);

        return mRootView;
    }

    public void getEventList() {
        Runnable load = new Runnable() {
            public void run() {
                try {
                    mEventList = mProvider.getEventListByType(1);
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
            mEventListAdapter.setEventList(mEventList);
            mEventListAdapter.notifyDataSetChanged();
        }
    };
}
