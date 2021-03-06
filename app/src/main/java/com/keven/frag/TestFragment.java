package com.keven.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keven.R;


public class TestFragment extends Fragment {

    private TextView textView;

    public static TestFragment newInstance(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("FRAGMENT_INDEX", index);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_test_item, null);
        textView = view.findViewById(R.id.text);
        textView.setText("FRAGMENT_INDEX = " + getArguments().getInt("FRAGMENT_INDEX"));

        Log.d("TAG", "********** TestFragment="+ TestFragment.this+"   index="+getArguments().getInt("FRAGMENT_INDEX"));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "++ "+ getParentFragment() +" , "+ getChildFragmentManager());
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container2, TestFragment.newInstance(getArguments().getInt("FRAGMENT_INDEX") + 1))
                        .addToBackStack(null)
                        .commit();
            }
        });

        view.findViewById(R.id.text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "-- "+ getParentFragment() +" , " + getFragmentManager() +" , "+ getChildFragmentManager());
                if (getParentFragment() != null) {
                    getParentFragment().getChildFragmentManager().popBackStack();
                } else if (getChildFragmentManager() != null) {
                    getChildFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }

    public void setState(String s) {
        textView.setText(s);
    }
}