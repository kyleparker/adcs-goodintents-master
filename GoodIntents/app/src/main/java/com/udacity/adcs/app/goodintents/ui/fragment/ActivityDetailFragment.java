package com.udacity.adcs.app.goodintents.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.ui.base.BaseFragment;

/**
 * Created by kyleparker on 11/9/2015.
 */
public class ActivityDetailFragment extends BaseFragment {

    /**
     * Factory method to generate a new instance of the fragment
     *
     * @return
     */
    public static ActivityDetailFragment newInstance() {
        final ActivityDetailFragment f = new ActivityDetailFragment();

//        final Bundle args = new Bundle();
//        args.putInt(Constants.Extra.INTRO_TYPE, introType);
//
//        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_activity_detail, container, false);
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return mRootView;
    }
}
