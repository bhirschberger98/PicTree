package com.bretthirschberger.pictree.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
    private EditText mSearchField;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mSearchField = root.findViewById(R.id.toolbar_search);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsReference = database.getReference(POSTS_REFERENCE);

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
                posts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    posts.add(0, post);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Database Error", "Failed to read value.", error.toException());
            }
        });

        mSearchField.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                postsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        posts.clear();
//                        if (mSearchField.getText().toString().equals(""))
//                            return;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getValue(Post.class).getDescription().toUpperCase().contains(mSearchField.getText().toString().toUpperCase())) {
                                Post post = snapshot.getValue(Post.class);
                                posts.add(0, post);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        if (posts.size() == 0)
                            Toast.makeText(getContext(), getResources().getText(R.string.not_found_label), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            }
            return false;
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