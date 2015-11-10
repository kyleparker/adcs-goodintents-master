package com.udacity.adcs.app.goodintents.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.ui.base.BaseFragment;
import com.udacity.adcs.app.goodintents.ui.list.EventListAdapter;

import java.util.ArrayList;

/**
 * Created by kyleparker on 11/9/2015.
 */
public class ProfileFragment extends BaseFragment {

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

        // Dummy Data
        ArrayList<Event> eventsList = new ArrayList<>();
        Event event = new Event();
        event.setName("Test Event");
        event.setDate(123456);
        event.setOrganization("Test Organization");

        eventsList.add(event);
        eventsList.add(event);
        eventsList.add(event);
        eventsList.add(event);
        eventsList.add(event);
        eventsList.add(event);
        eventsList.add(event);
        eventsList.add(event);
        eventsList.add(event);
        eventsList.add(event);

        RecyclerView eventsRecyclerView = (RecyclerView) mRootView.findViewById(R.id.events_recycler_view);
        EventListAdapter eventListAdapter = new EventListAdapter(eventsList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        eventsRecyclerView.setLayoutManager(linearLayoutManager);
        eventsRecyclerView.setAdapter(eventListAdapter);

        return mRootView;
    }
}
