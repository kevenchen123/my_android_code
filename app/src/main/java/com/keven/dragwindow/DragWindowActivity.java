package com.keven.dragwindow;

import android.os.Bundle;
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
        testDragWindow();
        testDragWindow();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void testDragWindow() {
        dragView = new DragView(this);
        View root = View.inflate(this, R.layout.activity_darg_window, dragView);
        ((TextView)root.findViewById(R.id.text)).setText("ABC" + i);
        dragView.show(500, 700);
        i++;
        //if (i == 3) dragView.setVisibility(View.GONE);
    }
}