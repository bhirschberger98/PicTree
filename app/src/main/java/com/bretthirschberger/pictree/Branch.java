package com.bretthirschberger.pictree;

final public class Branch extends Post {

    private Post mPreviousPost;

//    public Branch(int nodePlace, String image, User user,String uploadId, Post previousPost) {
//        super(nodePlace, image, user);
//        mPreviousPost = previousPost;
//    }

    public Post getPreviousPost() {
        return mPreviousPost;
    }
}
