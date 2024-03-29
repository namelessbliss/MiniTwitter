package com.app.nb.minitwitter.common;

public class Constants {

    public static final String API_MINITWIITER_BASE_URL = "https://www.minitwitter.com:3001/apiv1/";
    public static final String API_MINITWIITER_FILES_URL = "https://www.minitwitter.com/apiv1/uploads/photos/";

    //Preferences
    public static final String PREF_TOKEN = "PREF_TOKEN";
    public static final String PREF_USERNAME = "PREF_USERNAME";
    public static final String PREF_EMAIL = "PREF_EMAIL";
    public static final String PREF_PHOTO_URL = "PREF_PHOTO_URL";
    public static final String PREF_CREATED = "PREF_CREATED";
    public static final String PREF_ACTIVE = "PREF_ACTIVE";

    //Arguments
    public static final String TWEET_LIST_TYPE = "TWEET_LIST_TYPE";
    public static final int TWEET_LIST_ALL = 1;
    public static final int TWEET_LIST_FAVS = 2;
    public static final String ARG_TWEET_ID = "TWEET_ID";

    //startActivityForResult
    public static final int SELECT_PHOTO_GALLERY = 100;
}
