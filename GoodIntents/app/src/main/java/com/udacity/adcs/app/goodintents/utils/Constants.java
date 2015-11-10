package com.udacity.adcs.app.goodintents.utils;

import java.util.Locale;

/**
 * Created by kyleparker on 11/9/2015.
 */
public class Constants {
    private static final String PACKAGE = "com.udacity.adcs.app.goodintents";

    public static final String GCM_SERVER_API_KEY = "AIzaSyBwE_aRGSEyRh4Y4W6G_g9sxlAiLB12DKk";
    public static final String GCM_SENDER_ID = "469274212704";

    public static final String ACTION_DEEP_LINK_JOIN_ACTIVITY = PACKAGE + ".ACTION_DEEP_LINK_JOIN_ACTIVITY";
    public static final String ACTION_GCM_REGISTRATION_COMPLETE = PACKAGE + ".REGISTRATION_COMPLETE";

    public static final String APP_INVITE_DEEP_LINK_JOIN_TRIP = "/joinactivity/?";
    public static final String APP_INVITE_HOST = "http://goodintents.com";
    public static final String APP_INVITE_IMAGE_URL = "https://apps.bsu.edu/traveler/images/icon512x512.png";

    public static final Locale LOCALE_DEFAULT = Locale.US;

    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";

    public class Extra {
        private static final String PREFIX = ".extra.";

        public static final String LIST = ".LIST";
        public static final String ACTION = PACKAGE + PREFIX + "ACTION";
        public static final String ACTIVITY_ID = PACKAGE + PREFIX + "ACTIVITY_ID";
        public static final String EVENT_ID = PACKAGE + PREFIX + "EVENT_ID";
        public static final String FINISH_INTENT = PACKAGE + PREFIX + "FINISH_INTENT";
    }

    public class Message {
        public static final int NOT_FOUND = 1;
        public static final int FOUND = 2;
        public static final int ALREADY_ADDED = 3;
    }

    public class Post {
        public static final String AUTH_CATEGORY = PACKAGE + ".category.POST_AUTH";
    }

    public class RequestCode {
        public static final int CAMERA_IMAGE_CAPTURE = 100;
        public static final int CAMERA_VIDEO_CAPTURE = 200;
        public static final int PLAY_SERVICES_ERROR_DIALOG = 300;
        public static final int SELECT_PICTURE = 400;
        public static final int HANDLE_GMS = 500;
        public static final int RECOVER_FROM_AUTH_ERROR = 600;
        public static final int APP_INVITE = 700;
        public static final int SIGN_IN = 800;
    }

    public enum Action {
        LOGIN,
        LOGOUT
    }

    public enum MimeType {
        AUDIO ("audio/3gpp"),
        HTML ("text/html"),
        IMAGE ("image/*"),
        IMAGE_PNG ("image/png"),
        IMAGE_JPG ("image/jpeg"),
        TEXT ("text/plain"),
        VIDEO ("video/3gpp");

        private final String mimeType;

        MimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String getValue() {
            return mimeType;
        }
    }
}
