package com.bretthirschberger.pictree.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bretthirschberger.pictree.Post;
import com.bretthirschberger.pictree.R;
import com.bretthirschberger.pictree.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ProfileFragment extends Fragment {

    private static String POSTS_REFERENCE = "posts";


    private ProfileViewModel mProfileViewModel;
    private ImageView mProfilePic;
    private TextView mUsernameTextView;
    private TextView mRootAmtView;
//    private TextView mBranchAmtView;
//    private GridLayout mBranchLayout;
    private GridLayout mRootLayout;
    private TabHost mTabHost;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mProfileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsReference = database.getReference(POSTS_REFERENCE);
//        mBranchLayout = root.findViewById(R.id.profile_branches_tab);
        mRootLayout = root.findViewById(R.id.profile_roots_tab);
        mProfilePic = root.findViewById(R.id.profile_profile_pic);
        mTabHost = root.findViewById(R.id.profile_tabs);
        mUsernameTextView = root.findViewById(R.id.profile_page_username);
        mRootAmtView = root.findViewById(R.id.profile_page_root_amt);
//        mBranchAmtView = root.findViewById(R.id.profile_page_branch_amt);
        mTabHost.setup();

        TabHost.TabSpec spec = mTabHost.newTabSpec("Roots");
        spec.setContent(R.id.profile_roots_tab);
        spec.setIndicator(getResources().getString(R.string.profile_roots));
        mTabHost.addTab(spec);

        try {
            mProfilePic.setImageBitmap(new ImageDownloader().execute(new URL(User.getCurrentUser().getProfilePicture())).get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mUsernameTextView.setText(User.getCurrentUser().getUsername());
        mRootAmtView.setText(User.getCurrentUser().getRootAmt()+" Roots");
//        mBranchAmtView.setText(User.getCurrentUser().getBranchAmt()+" Branches");


        mProfileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        postsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                final float scale = getContext().getResources().getDisplayMetrics().density;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    Log.i("snapshot", snapshot.toString());
                    int dim = 150;
                    if (post.getUser().getUsername().equals(User.getCurrentUser().getUsername())) {
                        if (post.isRoot()) {
                            ImageView imageView = new ImageView(root.getContext());

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
                            ImageView imageView = new ImageView(root.getContext());
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
        return root;
    }

    private void addToBranches(ImageView imageView) {
        imageView.setRotation(90);
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