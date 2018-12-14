package com.keven.dragwindow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.keven.R;


public class DragWindowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testDragWindow();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void testDragWindow() {
        DragView dragView = new DragView(this);
        View.inflate(this, R.layout.activity_darg_window, dragView);
        dragView.show(500, 700);
    }
}