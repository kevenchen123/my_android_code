package com.keven.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.keven.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetTestActivity extends AppCompatActivity {

    @BindView(R.id.iv_cover) AutoLoadImageView iv_cover;
    @BindView(R.id.plurals_text) TextView plurals_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        iv_cover.setImageUrl("http://pic1.win4000.com/wallpaper/5/54055707675cb.jpg");
        plurals_text.setText(getResources().getQuantityString(R.plurals.orange, 10, 10));
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}