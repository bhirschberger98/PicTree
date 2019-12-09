package com.bretthirschberger.pictree.ui.posts;

import android.os.Bundle;
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

import com.bretthirschberger.pictree.LocalPostsAdapter;
import com.bretthirschberger.pictree.PictureDatabaseHandler;
import com.bretthirschberger.pictree.R;

public class PostsFragment extends Fragment {

    private PostsViewModel postViewModel;
    private RecyclerView mLocalPostsList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        ArrayList<LocalPost> posts = new ArrayList<>();
        postViewModel =
                ViewModelProviders.of(this).get(PostsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_local_posts, container, false);
//        final TextView textView = root.findViewById(R.id.text_send);
        postViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });

        mAdapter = new LocalPostsAdapter(new PictureDatabaseHandler(root.getContext(), null).getAllEntries());
        mLocalPostsList = root.findViewById(R.id.local_posts_view);
        mLayoutManager = new LinearLayoutManager(getContext());

        mLocalPostsList.setLayoutManager(mLayoutManager);
        mLocalPostsList.setAdapter(mAdapter);
        return root;
    }
}