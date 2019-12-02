package com.bretthirschberger.pictree.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bretthirschberger.pictree.Post;
import com.bretthirschberger.pictree.PostListAdapter;
import com.bretthirschberger.pictree.R;
import com.bretthirschberger.pictree.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView mPostsList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String POSTS_REFERENCE = "posts";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsReference = database.getReference();

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        ArrayList<Post> posts = new ArrayList<>();
        // Read from the database
        postsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post= new Post();
                    User user= new User();
                    Log.i("snapshot",snapshot.toString());
                    user.setEmail(snapshot.child("user").getValue(User.class).getEmail());
                    user.setProfilePicture(snapshot.child("user").getValue(User.class).getProfilePicture());
                    user.setEmail(snapshot.child("user").getValue(User.class).getEmail());
                    post.setUser(user);
                    post.setImage(snapshot.getValue(Post.class).getImage());
                    post.setPostTime(snapshot.getValue(Post.class).getPostTime());
                    Log.i("Image URI",post.getImage());
                    Log.i("Time",post.getPostTime());
                    posts.add(post);
//                post.getImage().trim();
//                post.getNodePlace();
//                post.getPostTime().toLocalDate();
//                post.getUser().getProfilePicture();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Database Error", "Failed to read value.", error.toException());
            }
        });
        mAdapter = new PostListAdapter(posts);
        mLayoutManager = new LinearLayoutManager(getContext());
        mPostsList = root.findViewById(R.id.post_list);
        mPostsList.setHasFixedSize(true);

        mPostsList.setLayoutManager(mLayoutManager);
        mPostsList.setAdapter(mAdapter);
        return root;

    }
}