package com.udacity.adcs.app.goodintents.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.content.AppProviderUtils;
import com.udacity.adcs.app.goodintents.objects.PersonEvent;
import com.udacity.adcs.app.goodintents.ui.base.BaseFragment;
import com.udacity.adcs.app.goodintents.ui.list.PersonEventListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyleparker on 11/9/2015.
 */
public class FriendListFragment extends BaseFragment {
    private List<PersonEvent> mEventList;
    private PersonEventListRecyclerViewAdapter mPersonEventListRecyclerViewAdapter;

    /**
     * Factory method to generate a new instance of the fragment
     *
     * @return
     */
    public static FriendListFragment newInstance() {
        final FriendListFragment f = new FriendListFragment();

//        final Bundle args = new Bundle();
//        args.putInt(Constants.Extra.INTRO_TYPE, introType);
//
//        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_friend_list, container, false);
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //Add recycler view to fragment
        RecyclerView rv_event_list = (RecyclerView) mRootView.findViewById(R.id.rv_friend_list);
        mPersonEventListRecyclerViewAdapter = new PersonEventListRecyclerViewAdapter(new ArrayList<PersonEvent>());
        rv_event_list.setAdapter(mPersonEventListRecyclerViewAdapter);
        //Start thread to get event list
        getFriendEventList();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_event_list.setLayoutManager(mLayoutManager);

        FloatingActionButton addButton = (FloatingActionButton) mRootView.findViewById(R.id.fab_friend_list);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "Test", Toast.LENGTH_SHORT).show();
            }
        });
        return mRootView;
    }

    public void getFriendEventList() {
        Runnable load = new Runnable() {
            public void run() {
                try {
                    mEventList = mProvider.getEventListByType(2);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mActivity.runOnUiThread(getEventListRunnable);
                }
            }
        };
        Thread thread = new Thread(null, load, "getFriendEventList");
        thread.start();
    }

    private final Runnable getEventListRunnable = new Runnable() {
        public void run() {
            mPersonEventListRecyclerViewAdapter.setEventList(mEventList);
            mPersonEventListRecyclerViewAdapter.notifyDataSetChanged();
        }
    };
}
