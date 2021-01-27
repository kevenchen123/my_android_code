package com.keven.widget.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.keven.R;
import com.keven.widget.WaveView1;

import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

public class Wave1Fragment extends Fragment {

    public static final String TAG = "Wave1Fragment";

    WaveView1 waveView;
    Button startBtn, speedBtn;
    TextView progressTv;

    RadioGroup radioGroup;
    RadioGroup radioGroupColor;
    RadioGroup radioGroupSpeed;

    int count = 0;
    private static final int max = 10;
    int speed = 5;

    public static Wave1Fragment newInstance() {
        Wave1Fragment fragment = new Wave1Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_wave_1, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
    }

    private void initView(View view) {
        waveView = (WaveView1) view.findViewById(R.id.waveview);
        startBtn = (Button) view.findViewById(R.id.button);
        speedBtn = (Button) view.findViewById(R.id.speed);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        radioGroupColor = (RadioGroup) view.findViewById(R.id.radio_group_color);
        radioGroupSpeed = (RadioGroup) view.findViewById(R.id.radio_group_speed);
        progressTv = (TextView) view.findViewById(R.id.pb_show_tv);

        waveView.setMax(max);
        waveView.setWaveColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
//        waveView.setProgress(10);
    }

    private void initListener() {
        final NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        waveView.setProgressListener(new WaveView1.waveProgressListener() {
            @Override
            public void onPorgress(boolean isDone, long progress, long max) {
                Log.i("yuan", "max " + max + "prgress " + progress);
                progressTv.setText(numberFormat.format(progress / (float) max * 100) + "%");
                if (isDone) {
                    Toast.makeText(getContext(), "Loading completed!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先重置
                resetTimer();
                doing();
            }
        });

        speedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p = (int) (waveView.getProgress() + 3);
                waveView.setProgress(p);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.mode_circle:
                        waveView.setMode(WaveView1.MODE_CIRCLE);
                        break;
                    case R.id.mode_rect:
                        waveView.setMode(WaveView1.MODE_RECT);
                        break;
                    case R.id.mode_drawable:
                        waveView.setMode(WaveView1.MODE_DRAWABLE);
                        break;
                }
            }
        });

        radioGroupColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.color_black:
                        waveView.setWaveColor(ContextCompat.getColor(getContext(), R.color.light_black));
                        break;
                    case R.id.color_red:
                        waveView.setWaveColor(ContextCompat.getColor(getContext(), R.color.light_red));
                        break;
                    case R.id.color_blue:
                        waveView.setWaveColor(ContextCompat.getColor(getContext(), R.color.light_blue));
                        break;
                    case R.id.color_green:
                        waveView.setWaveColor(ContextCompat.getColor(getContext(), R.color.light_green));
                        break;
                }
            }
        });

        radioGroupSpeed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.speed_normal:
                        waveView.setSpeed(WaveView1.SPEED_NORMAL);
                        break;
                    case R.id.speed_slow:
                        waveView.setSpeed(WaveView1.SPEED_SLOW);
                        break;
                    case R.id.speed_fast:
                        waveView.setSpeed(WaveView1.SPEED_FAST);
                        break;
                }
            }
        });
    }

    public void doing() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(100);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
    }

    private void resetTimer() {
        count = 0;
        if (timer != null)
            timer.cancel();
        timer = null;
        if (timerTask != null)
            timerTask.cancel();
        timerTask = null;
    }

    private Timer timer = new Timer();

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(100);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                if (max >= count) {
                    Log.d("demo", count + "");
                    waveView.setProgress(count);
                } else {
                    resetTimer();
                }
                count++;
            }
        }
    };
}