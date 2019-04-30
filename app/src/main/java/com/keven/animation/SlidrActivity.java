package com.keven.animation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.keven.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

public class SlidrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidr);

        // 设置右滑动返回
        int primary = getResources().getColor(R.color.colorPrimary);
        int secondary = getResources().getColor(R.color.colorAccent);

        Slidr.attach(this,  new SlidrConfig.Builder().primaryColor(primary)
            .secondaryColor(secondary)
            .scrimColor(Color.BLACK)
            .position(SlidrPosition.LEFT)
            .scrimStartAlpha(1.0f)
            .scrimEndAlpha(0f)
            .velocityThreshold(5f)
            .distanceThreshold(.35f)
            .build());

    }
}