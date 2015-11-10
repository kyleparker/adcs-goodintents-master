package com.udacity.adcs.app.goodintents.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.ui.base.BaseFragment;
import com.udacity.adcs.app.goodintents.ui.list.EventListAdapter;
import com.udacity.adcs.app.goodintents.ui.list.EventListRecyclerViewAdapter;

/**
 * Created by kyleparker on 11/9/2015.
 */
public class FriendListFragment extends BaseFragment {

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
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        rv_event_list.setLayoutManager(mLayoutManager);
        EventListRecyclerViewAdapter rv_adapter = new EventListRecyclerViewAdapter(new String[10]);
        rv_event_list.setAdapter(rv_adapter);
        return mRootView;
    }
}
