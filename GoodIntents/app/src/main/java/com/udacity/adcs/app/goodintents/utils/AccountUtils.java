package com.udacity.adcs.app.goodintents.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.ui.SignInActivity;

public class AccountUtils {

    public static boolean isAuthenticated(final Context context) {
        return !TextUtils.isEmpty(PreferencesUtils.getString(context, R.string.google_account_key, PreferencesUtils.GOOGLE_ACCOUNT_DEFAULT));
    }

    public static void startAuthenticationFlow(final Context context, final Intent finishIntent) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.Extra.ACTION, Constants.Action.LOGIN);
        intent.putExtra(Constants.Extra.FINISH_INTENT, finishIntent);
        context.startActivity(intent);
    }
}
