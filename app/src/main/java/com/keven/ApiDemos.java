package com.keven;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keven.animation.SlidrActivity;
import com.keven.api.AndroidApiActivity;
import com.keven.dragwindow.DragWindowActivity;
import com.keven.encrypt.EncryptActivity;
import com.keven.frag.FragmentPagerActivity;
import com.keven.nexus_httpserver.NexusTestActivity;
import com.keven.scrollpicker.ScrollpickerActivity;
import com.keven.touchevent.TouchEventActivity;
import com.keven.webview.WebActivity;


public class ApiDemos extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String nameData[ ]={"SystemAPI", "RsaEncrypt", "FragmentPager", "TouchEvent", "DragWindow"
                , "ScrollPicker", "Nexus & HttpServer", "Slidr", "WebView"};
        Intent intentData[ ]={
                new Intent(this, AndroidApiActivity.class),
                new Intent(this, EncryptActivity.class),
                new Intent(this, FragmentPagerActivity.class),
                new Intent(this, TouchEventActivity.class),
                new Intent(this, DragWindowActivity.class),
                new Intent(this, ScrollpickerActivity.class),
                new Intent(this, NexusTestActivity.class),
                new Intent(this, SlidrActivity.class),
                WebActivity.getStartIntent(this, "hello", "http://www.bing.com") };

        List<Map<String, Object>> myData = new ArrayList<>();
        for (int i = 0; i < nameData.length; i++) {
            addItem(myData, nameData[i], intentData[i]);
        }
        setListAdapter(new SimpleAdapter(this, myData,
                android.R.layout.simple_list_item_1, new String[] { "title" },
                new int[] { android.R.id.text1 }));
        getListView().setTextFilterEnabled(true);
    }


    protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>)l.getItemAtPosition(position);
        Intent intent = new Intent((Intent) map.get("intent"));
        startActivity(intent);
    }
}