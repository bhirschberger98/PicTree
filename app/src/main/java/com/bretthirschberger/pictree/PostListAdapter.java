package com.bretthirschberger.pictree;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ExamplePost> {
    private ArrayList<Post> mPosts;

    public static class ExamplePost extends RecyclerView.ViewHolder {
        private ImageView mProfilePicture;
        private ImageView mPostPicture;
        private TextView mUsername;

        public ExamplePost(@NonNull View itemView) {
            super(itemView);
            mProfilePicture = itemView.findViewById(R.id.post_profile_pic);
            mPostPicture = itemView.findViewById(R.id.picture_post);
            mUsername = itemView.findViewById(R.id.post_username);
        }
    }

    public PostListAdapter(ArrayList<Post> posts) {
        mPosts = posts;
    }

    @NonNull
    @Override
    public ExamplePost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post, parent, false);
        return new ExamplePost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamplePost holder, int position) {
        Post currentItem =mPosts.get(position);
        holder.mPostPicture.setImageDrawable(currentItem.getImage());
        holder.mProfilePicture.setImageDrawable(currentItem.getProfilePicture());
        holder.mUsername.setText(currentItem.getUsername());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        View view = inflater.inflate(R.layout.fragment_post, parent, false);
//
//        Post post = getItem(position);
//        ImageView profilePicture = view.findViewById(R.id.post_profile_pic);
//        ImageView postPicture = view.findViewById(R.id.picture_post);
//        TextView username = view.findViewById(R.id.post_username);
//
//        profilePicture.setImageDrawable(post.getProfilePicture());
//        postPicture.setImageDrawable(post.getImage());
//        username.setText(post.getUsername());
//        return view;
//    }
}
