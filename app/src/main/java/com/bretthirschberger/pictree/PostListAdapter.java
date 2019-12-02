package com.bretthirschberger.pictree;

import android.net.Uri;
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
        private TextView mNodePlace;
        private TextView mTime;

        public ExamplePost(@NonNull View itemView) {
            super(itemView);
            mProfilePicture = itemView.findViewById(R.id.post_profile_pic);
            mPostPicture = itemView.findViewById(R.id.picture_post);
            mUsername = itemView.findViewById(R.id.post_username);
            mNodePlace = itemView.findViewById(R.id.post_node_place);
            mTime = itemView.findViewById(R.id.post_time);
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
        holder.mPostPicture.setImageURI(Uri.parse(currentItem.getImage()));
        holder.mProfilePicture.setImageURI(Uri.parse(currentItem.getUser().getProfilePicture()));
        holder.mUsername.setText(currentItem.getUser().getUsername());
        holder.mNodePlace.setText(currentItem.getNodePlace()+"th node");
        holder.mTime.setText(currentItem.getPostTime());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

}
