package com.bretthirschberger.pictree;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.objects.FirebaseVisionObject;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ExamplePost> {
    private ArrayList<Post> mPosts;
    private static String POSTS_REFERENCE = "posts";
    private static final String USER_REFERENCE = "user";

    public static class ExamplePost extends RecyclerView.ViewHolder {
        private ImageView mProfilePicture;
        private ImageView mPostPicture;
        private TextView mUsername;
        private TextView mTime;
        private TextView mLikesText;
        private TextView mDescriptionView;
        private Button mFavoriteButton;
        private View mItemView;

        ExamplePost(@NonNull View itemView) {
            super(itemView);
            mProfilePicture = itemView.findViewById(R.id.post_profile_pic);
            mPostPicture = itemView.findViewById(R.id.local_picture_post);
            mUsername = itemView.findViewById(R.id.local_post_username);
            mTime = itemView.findViewById(R.id.local_post_time);
            mFavoriteButton = itemView.findViewById(R.id.post_like_button);
            mLikesText = itemView.findViewById(R.id.post_like_view);
            mDescriptionView = itemView.findViewById(R.id.local_post_description_view);
            mItemView = itemView;
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
        FirebaseVisionObjectDetectorOptions options =
                new FirebaseVisionObjectDetectorOptions.Builder()
                        .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()  // Optional
                        .build();
        FirebaseVisionObjectDetector objectDetector =
                FirebaseVision.getInstance().getOnDeviceObjectDetector(options);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Bitmap postPicture = null;
        try {
            postPicture = new ImageDownloader().execute(new URL(currentItem.getImage())).get();
        } catch (InterruptedException | ExecutionException | MalformedURLException e) {
            e.printStackTrace();
        }
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(postPicture);
        DatabaseReference postsReference = database.getReference(POSTS_REFERENCE);
        DatabaseReference usersReference = database.getReference(USER_REFERENCE);

        try {
            holder.mProfilePicture.setImageBitmap(new ImageDownloader().execute(new URL(currentItem.getUser().getProfilePicture())).get());
        } catch (InterruptedException | ExecutionException | MalformedURLException e) {
            e.printStackTrace();
        }

        holder.mPostPicture.setImageBitmap(postPicture);
        Bitmap finalPostPicture = postPicture.copy(postPicture.getConfig(), true);
        objectDetector.processImage(image)
                .addOnSuccessListener(detectedObjects -> {
                    Canvas canvas = new Canvas(finalPostPicture);
                    Paint rectPaint = new Paint();
                    Paint textPaint = new Paint();
                    textPaint.setTextSize(100);
                    textPaint.setColor(Color.RED);
//                    textPaint.
                    rectPaint.setStyle(Paint.Style.STROKE);
                    rectPaint.setStrokeWidth(20);
                    rectPaint.setColor(Color.RED);
                    for (FirebaseVisionObject visionObject : detectedObjects) {
                        canvas.drawRect(visionObject.getBoundingBox(), rectPaint);
                        String category;
                        switch (visionObject.getClassificationCategory()) {
                            case FirebaseVisionObject.CATEGORY_FASHION_GOOD:
                                category = "Fashion Good";
                                break;
                            case FirebaseVisionObject.CATEGORY_FOOD:
                                category = "Food";
                                break;
                            case FirebaseVisionObject.CATEGORY_HOME_GOOD:
                                category = "Home Good";
                                break;
                            case FirebaseVisionObject.CATEGORY_PLACE:
                                category = "Place";
                                break;
                            case FirebaseVisionObject.CATEGORY_PLANT:
                                category = "Plant";
                                break;
                            default:
                                category = "Unknown";

                        }
                        canvas.drawText("Category: " + category +
                                        ", Confidence: " + visionObject.getClassificationConfidence(),
                                visionObject.getBoundingBox().left,
                                visionObject.getBoundingBox().top,
                                textPaint);
                    }
                    Log.i("Objects Found", detectedObjects.size() + "");
                    holder.mPostPicture.setImageBitmap(finalPostPicture);
                }).addOnFailureListener(e -> {
        });
        holder.mUsername.setText(currentItem.getUser().getUsername());
        holder.mTime.setText(currentItem.getPostTime());

        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getCloudImageLabeler();
        labeler.processImage(image)
                .addOnSuccessListener(detectedObjects -> {
                    String description = "";
                    for (FirebaseVisionImageLabel visionObject : detectedObjects) {
                        description += visionObject.getText() + " ";
                    }
                    holder.mDescriptionView.setText(description);
                }).addOnFailureListener(e -> holder.mDescriptionView.setText(holder.mItemView.getResources().getText(R.string.description_error)));


        holder.mFavoriteButton.setOnClickListener(v -> postsReference.child(currentItem.getPostId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postsReference.child(currentItem.getPostId()).child("likes").setValue(dataSnapshot.getValue(Post.class).getLikes() + 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));
        postsReference.child(currentItem.getPostId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int likes = dataSnapshot.getValue(Post.class).getLikes();
                holder.mLikesText.setText(likes + (likes == 1 ? " Like" : " Likes"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.mProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(holder.mItemView.getContext(), UserProfileActivity.class);
            intent.putExtra(USER_REFERENCE, currentItem.getUser());
            holder.mItemView.getContext().startActivity(intent);
        });
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
