package com.bretthirschberger.pictree;

import android.graphics.drawable.Drawable;

final public class Post {
    private Drawable mImage;
    private Drawable mProfilePicture;
    private String mUsername;
    private String[] mComments;

    public Post(Drawable profilePicture, Drawable image, String username, String[] comments) {
        mImage = image;
        mComments = comments;
        mUsername = username;
        mProfilePicture = profilePicture;
    }

    public Drawable getProfilePicture() {
        return mProfilePicture;
    }

    public String getUsername() {
        return mUsername;
    }

    public Drawable getImage() {
        return mImage;
    }

    public String[] getComments() {
        return mComments;
    }
}
