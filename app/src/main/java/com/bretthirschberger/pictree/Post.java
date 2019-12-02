package com.bretthirschberger.pictree;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Post {
    private String mImageUri;
    private User mUser;
    private String mPostTime;
    private ArrayList<Comment> mComments;
    private int mNodePlace;

    public Post(){
        //Required empty constructor
    }

    public Post(int nodePlace, String imageUri, User user) {
        mNodePlace = nodePlace;
        mImageUri = imageUri;
        mUser = user;
        mPostTime = LocalDateTime.now().toString();
    }

    public Post(int nodePlace, String imageUri, User user, ArrayList<Comment> comments) {
        mNodePlace = nodePlace;
        mImageUri = imageUri;
        mComments = comments;
        mUser = user;
        mPostTime = LocalDateTime.now().toString();
    }

    public User getUser() {
        return mUser;
    }

    public String getImage() {
        return mImageUri;
    }

    public void addComment(Comment comment) {
        mComments.add(comment);
    }

    public String getPostTime() {
        return mPostTime;
    }

    public int getNodePlace() {
        return mNodePlace;
    }

    public ArrayList<Comment> getComments() {
        return mComments;
    }

    public void setImage(String imageUri) {
        mImageUri = imageUri;
    }

    public void setNodePlace(int nodePlace) {
        mNodePlace = nodePlace;
    }

    public void setPostTime(String postTime) {
        mPostTime = postTime;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
