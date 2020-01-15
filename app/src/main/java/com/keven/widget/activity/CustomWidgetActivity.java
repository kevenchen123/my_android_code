package com.keven.widget.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.keven.R;


public class CustomWidgetActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String[] ITEMS = {"Sliding Drawer 1", "XXX", "XXX"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Custom Widget");
        setContentView(R.layout.activity_custom_widget);

        ViewGroup content = findViewById(R.id.content);
        ListView list = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ITEMS);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        content.addView(list);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                FragmentManager fragmentManager = getSupportFragmentManager();
                SlidingDrawerFragment fragment = SlidingDrawerFragment.newInstance();
                fragmentManager.beginTransaction().replace(
                        R.id.content_fragment,
                        fragment,
                        SlidingDrawerFragment.TAG)
                        .commit();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                FragmentManager fragmentManager = getSupportFragmentManager();
                final SlidingDrawerFragment fragment = (SlidingDrawerFragment)fragmentManager.findFragmentByTag(SlidingDrawerFragment.TAG);
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    return true;
                }
            default:
                return super.onKeyDown(keyCode, event);
        }
    }
}