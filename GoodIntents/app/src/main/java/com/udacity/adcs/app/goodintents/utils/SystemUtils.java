package com.udacity.adcs.app.goodintents.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utility class for accessing basic Android functionality.
 */
public class SystemUtils {

	/**
	 * Check the wifi state of the device - if not on, send the user to the settings to enable
	 * 
	 * @param context
	 */
	public static boolean isOnline(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mgr.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected() && networkInfo.getState() == NetworkInfo.State.CONNECTED;
	}
}