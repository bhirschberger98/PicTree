package com.bretthirschberger.pictree.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TabHost;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bretthirschberger.pictree.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mProfileViewModel;
    private GridLayout mBranchLayout;
    private GridLayout mRootLayout;
    private TabHost mTabHost;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mProfileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mBranchLayout = root.findViewById(R.id.profile_branches_tab);
        mRootLayout = root.findViewById(R.id.profile_roots_tab);
        mTabHost = root.findViewById(R.id.profile_tabs);
        mTabHost.setup();

        TabHost.TabSpec spec = mTabHost.newTabSpec("Branches");
        spec.setContent(R.id.profile_branches_tab);
        spec.setIndicator(getResources().getString(R.string.profile_branches));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec("Roots");
        spec.setContent(R.id.profile_roots_tab);
        spec.setIndicator(getResources().getString(R.string.profile_roots));
        mTabHost.addTab(spec);

        mProfileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        populateBranches();
        populateRoots();
        return root;
    }

    private void populateBranches() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_dark));
        mBranchLayout.addView(imageView);
//        mBranchLayout.addView(imageView);
//        mBranchLayout.addView(imageView);
//        mBranchLayout.addView(imageView);
    }

    private void populateRoots() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_disabled));
        mRootLayout.addView(imageView);
//        mRootLayout.addView(imageView);
//        mRootLayout.addView(imageView);
//        mRootLayout.addView(imageView);
    }
}