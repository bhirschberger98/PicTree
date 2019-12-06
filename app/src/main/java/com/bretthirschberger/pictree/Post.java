package com.bretthirschberger.pictree;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Post {
    private String mPostId;
    private String mImageUri;
    private String mDescription;
    private User mUser;
    private String mPostTime;
    private ArrayList<Comment> mComments;
    private Post mPreviousPost;
    private int mNodePlace;
    private int mLikes;
    private boolean mIsRoot;

    public Post() {
        //Required empty constructor
    }

    public Post(int nodePlace, String imageUri, String description, User user, String postId) {
        mNodePlace = nodePlace;
        mImageUri = imageUri;
        mUser = user;
        mDescription=description;
        mPostTime = LocalDateTime.now().toString();
        mPostId = postId;
        mIsRoot = nodePlace == 1;
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

    public boolean isRoot() {
        return mIsRoot;
    }

    public void setPreviousPost(Post previousPost) {
        mPreviousPost = previousPost;
    }

    public Post getPreviousPost() {
        return mPreviousPost;
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

    public void setRoot(boolean root) {
        mIsRoot = root;
    }

    public void setComments(ArrayList<Comment> comments) {
        mComments = comments;
    }

    public int getLikes() {
        return mLikes;
    }

    public void setLikes(int likes) {
        mLikes = likes;
    }

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        mPostId = postId;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }
}

