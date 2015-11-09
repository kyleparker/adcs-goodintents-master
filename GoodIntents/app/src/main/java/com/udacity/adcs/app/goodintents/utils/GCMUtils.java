/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udacity.adcs.app.goodintents.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public final class GCMUtils {
	private static final int MAX_ATTEMPTS = 5;
	private static final int BACKOFF_MILLI_SECONDS = 2000;
	private static final Random random = new Random();

	private GCMUtils() {
	}

	/**
	 * Unregister the current GCM ID when we sign-out
	 * 
	 * @param context Current context
	 */
	public static void onSignOut(Context context) {
	}

	/**
	 * Register this account/device pair within the server.
	 * 
	 * @return whether the registration succeeded or not.
	 */
	public static boolean sendRegistrationToServer(final Context context, final String token) {

		return false;
	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	public static boolean unregister(final Context context) {
		return false;
	}
	
	/** 
	 * Issue a POST request to the server.
	 * 
	 * @param context
	 * @param endpoint
	 * @param params
     * @param userGuid
     * @param token
	 * 
	 * @return success (post operation returned a 200 status or otherwise)
	 * 
	 * @throws IOException propagated from POST.
	 */
	private static boolean post(Context context, String endpoint, Map<String, String> params, String userGuid, String token)
            throws IOException {
		return false;
	}
}
