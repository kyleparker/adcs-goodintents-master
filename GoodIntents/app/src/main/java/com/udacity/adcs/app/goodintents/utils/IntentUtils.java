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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.udacity.adcs.app.goodintents.R;

import java.io.File;

/**
 * Utilities for creating intents.
 * 
 * @author Jimmy Shih
 */
public class IntentUtils {

	/**
	 * Creates an intent with {@link Intent#FLAG_ACTIVITY_CLEAR_TOP} and {@link Intent#FLAG_ACTIVITY_NEW_TASK}.
	 * 
	 * @param context the context
	 * @param cls the class
	 */
	public static Intent newIntent(Context context, Class<?> cls) {
		try {
			return new Intent(context, cls).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		} catch (Exception ex) {
			return new Intent();
		}
	}

	/**
	 * Handle the share intent for a digital asset
	 * 
	 * @param activity
	 * @param subject
	 * @param message
	 * @param mimeType
	 * @param uri
	 */
	public static void newShareIntent(Activity activity, String subject, String message, Constants.MimeType mimeType, Uri uri) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(mimeType.getValue());
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, message);
		
		switch (mimeType) {
			case AUDIO:
			case IMAGE:
			case VIDEO:
				intent.putExtra(Intent.EXTRA_STREAM, uri);
				break;
			default:
				break;
		}
		
		activity.startActivity(Intent.createChooser(intent, activity.getText(R.string.content_share_path_picker_title)));
	}

	/**
	 * Start the media scanner for the recently added media file
	 * @param context
	 * @param path
	 */
	public static void startMediaScanner(Context context, String path) {
		// Add media to the device gallery (MediaStore)
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		intent.setData(Uri.fromFile(new File(path)));
		context.sendBroadcast(intent);
	}
}
