package com.example.daggerpractice.ui.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.daggerpractice.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dagger.android.support.DaggerFragment;

public class ProfileFragment extends DaggerFragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toast.makeText(getActivity(), "Profile Fragment", Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}