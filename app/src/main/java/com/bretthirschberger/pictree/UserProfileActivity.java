package com.bretthirschberger.pictree;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.bretthirschberger.pictree.ui.profile.ProfileViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class UserProfileActivity extends AppCompatActivity {

    private static String POSTS_REFERENCE = "posts";
    private static final String USER_REFERENCE = "user";

    private ProfileViewModel mProfileViewModel;
    private ImageView mProfilePic;
    private TextView mUsernameTextView;
    private TextView mRootAmtView;
//    private TextView mBranchAmtView;
//    private GridLayout mBranchLayout;
    private GridLayout mRootLayout;
    private TabHost mTabHost;
    private User mUser;
    private DatabaseReference mPostsReference;
    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsReference = database.getReference(POSTS_REFERENCE);
        mUserReference = database.getReference(USER_REFERENCE);
        mUsernameTextView = findViewById(R.id.profile_page_username);
//        mBranchLayout = findViewById(R.id.profile_branches_tab);
        mRootLayout = findViewById(R.id.profile_roots_tab);
        mProfilePic = findViewById(R.id.profile_profile_pic);
        mTabHost = findViewById(R.id.profile_tabs);
        mRootAmtView = findViewById(R.id.profile_page_root_amt);
        mUser = (User) getIntent().getSerializableExtra(USER_REFERENCE);

        mTabHost.setup();

        TabHost.TabSpec spec = mTabHost.newTabSpec("Roots");
        spec.setContent(R.id.profile_roots_tab);
        spec.setIndicator(getResources().getString(R.string.profile_roots));
        mTabHost.addTab(spec);

        mUsernameTextView.setText(mUser.getUsername());
        mRootAmtView.setText(mUser.getRootAmt() + " Roots");
//        mBranchAmtView.setText(mUser.getBranchAmt() + " Branches");
        try {
            mProfilePic.setImageBitmap(new ImageDownloader().execute(new URL(mUser.getProfilePicture())).get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        postsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    Log.i("snapshot", snapshot.toString());
                    int dim = 150;
                    if (post.getUser().getUsername().equals(mUser.getUsername())) {
                        if (post.isRoot()) {
                            ImageView imageView = new ImageView(getApplicationContext());
                            try {
                                imageView.setImageBitmap(new ImageDownloader().execute(new URL(post.getImage())).get());
                                imageView.setLayoutParams(new LinearLayout.LayoutParams(dim * (int) scale, dim * (int) scale));
                                imageView.setPadding(8 * (int) scale, 8 * (int) scale, 8 * (int) scale, 8 * (int) scale);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            addToRoots(imageView);
                        } else {
                            ImageView imageView = new ImageView(getApplicationContext());
                            try {
                                imageView.setImageBitmap(new ImageDownloader().execute(new URL(post.getImage())).get());
                                imageView.setLayoutParams(new LinearLayout.LayoutParams(dim * (int) scale, dim * (int) scale));
                                imageView.setPadding(8 * (int) scale, 8 * (int) scale, 8 * (int) scale, 8 * (int) scale);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            addToBranches(imageView);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Database Error", "Failed to read value.", error.toException());
            }
        });
    }

    private void addToBranches(ImageView imageView) {
        imageView.setRotation(270);
//        mBranchLayout.addView(imageView);
    }

    private void addToRoots(ImageView imageView) {
        imageView.setRotation(90);
        mRootLayout.addView(imageView);
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
