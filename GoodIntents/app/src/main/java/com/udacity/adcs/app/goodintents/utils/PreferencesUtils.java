/*
 * Copyright 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.udacity.adcs.app.goodintents.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Utilities to access preferences stored in {@link SharedPreferences}.
 */
public class PreferencesUtils {
	private static final String PREFS = "goodintent_prefs";

	public static final String GOOGLE_ACCOUNT_DEFAULT = "";

	public static class Tab {
		// Tab definitions for the feed activity
		public static final int FEED_LIST_EVENT = 0;
		public static final int FEED_LIST_FRIENDS = 1;
		public static final int FEED_LIST_PROFILE = 2;
	}

    /**
	 * Gets a preference key
	 * 
	 * @param context the context
	 * @param keyId the key id
	 */
	public static String getKey(Context context, int keyId) {
		return context.getString(keyId);
	}

	/**
	 * Gets a boolean preference value.
	 * 
	 * @param context the context
	 * @param keyId the key id
	 * @param defaultValue the default value
	 */
	public static boolean getBoolean(Context context, int keyId, boolean defaultValue) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
			return sharedPreferences.getBoolean(getKey(context, keyId), defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 * Sets a boolean preference value.
	 * 
	 * @param context the context
	 * @param keyId the key id
	 * @param value the value
	 */
	public static void setBoolean(Context context, int keyId, boolean value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(getKey(context, keyId), value);
		editor.apply();
	}

	/**
	 * Gets an double preference value.
	 * 
	 * @param context the context
	 * @param keyId the key id
	 * @param defaultValue the default value
	 */
	public static float getFloat(Context context, int keyId, float defaultValue) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
			return sharedPreferences.getFloat(getKey(context, keyId), defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 * Sets an integer preference value.
	 * 
	 * @param context the context
	 * @param keyId the key id
	 * @param value the value
	 */
	public static void setFloat(Context context, int keyId, float value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putFloat(getKey(context, keyId), value);
		editor.apply();
	}

	/**
	 * Gets an integer preference value.
	 * 
	 * @param context the context
	 * @param keyId the key id
	 * @param defaultValue the default value
	 */
	public static int getInt(Context context, int keyId, int defaultValue) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
			return sharedPreferences.getInt(getKey(context, keyId), defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 * Sets an integer preference value.
	 * 
	 * @param context the context
	 * @param keyId the key id
	 * @param value the value
	 */
	public static void setInt(Context context, int keyId, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt(getKey(context, keyId), value);
		editor.apply();
	}

	/**
	 * Gets a long preference value.
	 * 
	 * @param context the context
	 * @param keyId the key id
	 */
	public static long getLong(Context context, int keyId) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
			return sharedPreferences.getLong(getKey(context, keyId), -1L);
		} catch (Exception ex) {
			ex.printStackTrace();
			return keyId;
		}
	}

	/**
	 * Sets a long preference value.
	 * 
	 * @param context the context
	 * @param keyId the key id
	 * @param value the value
	 */
	public static void setLong(Context context, int keyId, long value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putLong(getKey(context, keyId), value);
		editor.apply();
	}

	/**
	 * Gets a string preference value.
	 * 
	 * @param context the context
	 * @param keyId the key id
	 * @param defaultValue default value
	 */
	public static String getString(Context context, int keyId, String defaultValue) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
			return sharedPreferences.getString(getKey(context, keyId), defaultValue);
		} catch (Exception ex) {
			ex.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 * Sets a string preference value.
	 * 
	 * @param context the context
	 * @param keyId the key id
	 * @param value the value
	 */
	public static void setString(Context context, int keyId, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(getKey(context, keyId), value);
		editor.apply();
	}
}
