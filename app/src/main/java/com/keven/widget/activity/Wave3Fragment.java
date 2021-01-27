package com.keven.widget.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keven.R;
import com.keven.widget.waveline.WaveLineView2;


public class Wave3Fragment extends Fragment {

    public static final String TAG = "Wave3Fragment";
    private WaveLineView2 waveLineView;

    public static Wave3Fragment newInstance() {
        Wave3Fragment fragment = new Wave3Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_wave_3, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        waveLineView = (WaveLineView2) view.findViewById(R.id.waveLineView2);

        view.findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveLineView.startAnim();
            }
        });
        view.findViewById(R.id.stopBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveLineView.stopAnim();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        waveLineView.release();
    }
}