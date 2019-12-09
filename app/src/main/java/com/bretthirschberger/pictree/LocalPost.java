package com.bretthirschberger.pictree;

import android.net.Uri;

public class LocalPost {
    private String mUserName;
    private String mImage;
    private String mDescription;
    private String mTime;

    public LocalPost(String userName, String image, String description, String time) {
        mUserName = userName;
        mImage = image;
        mDescription = description;
        mTime = time;
    }

    public String getUserName() {
        return mUserName;
    }

    public Uri getImageUri() {
        return Uri.parse(mImage);
    }

    public String getDescription() {
        return mDescription;
    }

    public String getTime() {
        return mTime;
    }
}
