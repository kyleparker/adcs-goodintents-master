package com.udacity.adcs.app.goodintents.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.adcs.app.goodintents.ui.base.BaseFragment;

/**
 * Created by kyleparker on 11/9/2015.
 */
public class EventDetailFragment extends BaseFragment {
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
        return mRootView;
    }

}
