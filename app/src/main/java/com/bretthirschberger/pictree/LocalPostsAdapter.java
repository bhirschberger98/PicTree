package com.bretthirschberger.pictree;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocalPostsAdapter extends RecyclerView.Adapter<LocalPostsAdapter.ExampleLocalPost>{

    private ArrayList<LocalPost> mLocalPosts;

    public static class ExampleLocalPost extends RecyclerView.ViewHolder {

        private ImageView mPostView;
        private TextView mUsername;
        private TextView mTime;
        private TextView mDescriptionView;

        ExampleLocalPost(@NonNull View itemView) {
            super(itemView);
            mPostView=itemView.findViewById(R.id.local_picture_post);
            mUsername=itemView.findViewById(R.id.local_post_username);
            mTime=itemView.findViewById(R.id.local_post_time);
            mDescriptionView=itemView.findViewById(R.id.local_post_description_view);
        }
    }

    public LocalPostsAdapter(ArrayList<LocalPost> localPosts) {
        mLocalPosts = localPosts;
    }

    @NonNull
    @Override
    public ExampleLocalPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_local_post_item, parent, false);
        return new ExampleLocalPost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleLocalPost holder, int position) {
        LocalPost currentItem=mLocalPosts.get(position);
        holder.mPostView.setImageURI(currentItem.getImageUri());
        holder.mUsername.setText(currentItem.getUserName());
        holder.mTime.setText(currentItem.getTime());
        holder.mDescriptionView.setText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return mLocalPosts.size();
    }
}
