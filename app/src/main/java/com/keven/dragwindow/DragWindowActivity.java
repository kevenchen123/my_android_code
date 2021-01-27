package com.keven.dragwindow;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.keven.R;


public class DragWindowActivity extends AppCompatActivity {
    private static int i =1;
    DragView dragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestOverlayPermission();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //在程序退出(Activity销毁）时销毁悬浮窗口
        dragView.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            testDragWindow();
            testDragWindow();
        }
    }

    private void requestOverlayPermission() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            testDragWindow();
            testDragWindow();
            return;
        }
        Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        myIntent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(myIntent, 100);
    }

    private void testDragWindow() {
        dragView = new DragView(this);
        View root = View.inflate(this, R.layout.activity_darg_window, dragView);
        ((TextView)root.findViewById(R.id.text)).setText("ABC" + i);
        dragView.show(500, 700);
        i++;
        //if (i == 3) dragView.setVisibility(View.GONE);
    }
}