package com.bretthirschberger.pictree;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

final public class User implements Serializable {

    private static User sCurrentUser;

    private String mUsername;
    private String mEmail;
    private String mUserId;
    private ArrayList<User> mFollowing;
    private String mProfilePictureUri;
    private int mRootAmt;
    private int mBranchAmt;
    private int mFollowersAmt;

    public User(){
        mFollowing=new ArrayList<>();
    }

    public User(String username, String email, Uri profilePicture) {
        mUsername = username;
        mEmail = email;
        mProfilePictureUri = profilePicture.toString();
        mFollowing=new ArrayList<>();
    }

    public User(String username, String email, String userId, Uri profilePicture) {
        mUsername = username;
        mEmail = email;
        mProfilePictureUri = profilePicture.toString();
        mUserId=userId;
        mFollowing=new ArrayList<>();
    }

    public ArrayList<User> getFollowing() {
        return mFollowing;
    }

    public void setFollowing(ArrayList<User> following) {
        mFollowing = following;
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

    public void setEmail(String email) {
        mEmail = email;
    }

    public static User getCurrentUser() {
        return sCurrentUser;
    }

    public static void setCurrentUser(User currentUser) {
        sCurrentUser = currentUser;
    }


    public void setProfilePicture(String profilePictureUri) {
        mProfilePictureUri = profilePictureUri;
    }

    public void setRootAmt(int rootAmt) {
        mRootAmt = rootAmt;
    }

    public int getRootAmt() {
        return mRootAmt;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setProfilePictureUri(String profilePictureUri) {
        mProfilePictureUri = profilePictureUri;
    }

    public void setBranchAmt(int branchAmt) {
        mBranchAmt = branchAmt;
    }

    public int getFollowersAmt() {
        return mFollowersAmt;
    }

    public void setFollowersAmt(int folowersAmt) {
        mFollowersAmt = folowersAmt;
    }

    public int getBranchAmt() {
        return mBranchAmt;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUserId() {
        return mUserId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof User){
            User user= (User)obj;
            return user.getUsername().equals(this.mUsername) && user.getEmail().equals(this.mEmail);
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return getEmail()+" "+getUsername();
    }
}
