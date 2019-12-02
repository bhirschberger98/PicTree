package com.bretthirschberger.pictree;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        Post currentItem = mPosts.get(position);
        try {
            holder.mPostPicture.setImageBitmap(new ImageDownloader().execute(new URL(currentItem.getImage())).get());
            holder.mProfilePicture.setImageBitmap(new ImageDownloader().execute(new URL(currentItem.getUser().getProfilePicture())).get());
        } catch (InterruptedException | ExecutionException | MalformedURLException e) {
            e.printStackTrace();
        }
        holder.mUsername.setText(currentItem.getUser().getUsername());
        holder.mNodePlace.setText(currentItem.getNodePlace() + "th node");
        holder.mTime.setText(currentItem.getPostTime());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    private class ImageDownloader extends AsyncTask<URL, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... urls) {

            try {
                return BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
