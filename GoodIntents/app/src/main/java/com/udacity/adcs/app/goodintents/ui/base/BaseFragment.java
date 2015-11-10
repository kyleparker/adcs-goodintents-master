package com.udacity.adcs.app.goodintents.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.udacity.adcs.app.goodintents.content.AppProviderUtils;

/**
 * Created by kyleparker on 11/9/2015.
 */
public abstract class BaseFragment extends Fragment {
    protected static Activity mActivity;
    protected static AppProviderUtils mProvider;

    protected ViewGroup mRootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mProvider = AppProviderUtils.Factory.get(mActivity);
    }
}
