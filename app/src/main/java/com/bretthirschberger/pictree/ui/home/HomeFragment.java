package com.bretthirschberger.pictree.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

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

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView mPostsList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        ArrayList<Post> posts=new ArrayList<>();
        //test entries will update dynamically later
        posts.add(new Post(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light_focused),getResources().getDrawable(R.drawable.common_google_signin_btn_icon_disabled),"Username",null));
        posts.add(new Post(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light_focused),getResources().getDrawable(R.drawable.common_google_signin_btn_icon_disabled),"Username",null));
        posts.add(new Post(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light_focused),getResources().getDrawable(R.drawable.common_google_signin_btn_icon_disabled),"Username",null));
        posts.add(new Post(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light_focused),getResources().getDrawable(R.drawable.common_google_signin_btn_icon_disabled),"Username",null));
        posts.add(new Post(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light_focused),getResources().getDrawable(R.drawable.common_google_signin_btn_icon_disabled),"Username",null));

        mAdapter = new PostListAdapter(posts);
        mLayoutManager = new LinearLayoutManager(getContext());
        mPostsList = root.findViewById(R.id.post_list);
        mPostsList.setHasFixedSize(true);

        mPostsList.setLayoutManager(mLayoutManager);
        mPostsList.setAdapter(mAdapter);
        return root;

    }
}