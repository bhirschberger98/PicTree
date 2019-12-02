package com.bretthirschberger.pictree;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

final public class User implements Serializable {

    private static User sCurrentUser;

    private String mUsername;
    private String mEmail;
    private ArrayList<User> mFollowing;
    private String mProfilePictureUri;

    public User(){

    }

    public User(String username, String email, Uri profilePicture) {
        mUsername = username;
        mEmail = email;
        mProfilePictureUri = profilePicture.toString();
    }

    public ArrayList<User> getFollowing() {
        return mFollowing;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getProfilePicture() {
        return mProfilePictureUri;
    }

    public String getEmail() {
        return mEmail;
    }

    public static User getCurrentUser() {
        return sCurrentUser;
    }

    public static void setCurrentUser(User currentUser) {
        sCurrentUser = currentUser;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setProfilePicture(String profilePictureUri) {
        mProfilePictureUri = profilePictureUri;
    }

    public void setUsername(String username) {
        mUsername = username;
    }
}
