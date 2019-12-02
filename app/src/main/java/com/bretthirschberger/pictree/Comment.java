package com.bretthirschberger.pictree;

import java.io.Serializable;
import java.time.LocalDateTime;

public final class Comment implements Serializable {
    private String mUsername;
    private String mComment;
    private LocalDateTime mTimePosted;

    public Comment(User user, String comment) {
        mUsername = user.getUsername();
        mComment = comment;
        mTimePosted=LocalDateTime.now();
    }

    public String getUsername() {
        return mUsername;
    }

    public String getComment() {
        return mComment;
    }

    public LocalDateTime getTimePosted() {
        return mTimePosted;
    }
}
