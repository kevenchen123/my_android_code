package com.keven.widget.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keven.R;

public class SlidingDrawerFragment extends Fragment {

    public static final String TAG = "SlidingDrawerFragment";
    public static final String ARG_STICK_TO = "stickTo";

    public static SlidingDrawerFragment newInstance() {
        SlidingDrawerFragment fragment = new SlidingDrawerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STICK_TO, 0);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        final Bundle args = getArguments();
        int resource = 0;
        switch (args.getInt(ARG_STICK_TO)) {
            case 0:
                resource = R.layout.fragment_sliding_drawer;
                break;
        }
        return inflater.inflate(resource, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
    }
}