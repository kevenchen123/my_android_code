package com.keven;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.decodeEncode.EncodeDecodeActivity;
import com.keven.animation.SlidrActivity;
import com.keven.api.AndroidApiActivity;
import com.keven.camera.CameraAlbumActivity;
import com.keven.dragwindow.DragWindowActivity;
import com.keven.encrypt.EncryptActivity;
import com.keven.frag.FragmentPagerActivity;
import com.keven.graphiclock.GraphicLockActivity;
import com.keven.hotfix.HotfixActivity;
import com.keven.nexus_httpserver.NexusTestActivity;
import com.keven.qrcode.QRCodeScanActivity;
import com.keven.scrollpicker.ScrollpickerActivity;
import com.keven.socket.SocketIOActivity;
import com.keven.touchevent.TouchEventActivity;
import com.keven.utils.LogUtils;
import com.keven.webview.WebActivity;
import com.keven.widget.activity.CustomWidgetActivity;
import com.keven.widget.activity.WidgetTestActivity;
import com.xmcMediacodec.receivedecode.ReceiveActivity;
import com.xmcMediacodec.sendencode.SendActivity;


public class ApiDemos extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String nameData[ ]={"SystemAPI", "Widget", "Custom Widget", "RsaEncrypt", "FragmentPager", "TouchEvent", "DragWindow",
                 "ScrollPicker", "Nexus & HttpServer", "Encode & Decode", "RTP sender", "RTP receiver", "QRcode", "Camera & Album",
                 "GraphicLock", "SocketIO", "Slidr", "HotFix", "WebView"};
        Intent intentData[ ]={
                new Intent(this, AndroidApiActivity.class),
                new Intent(this, WidgetTestActivity.class),
                new Intent(this, CustomWidgetActivity.class),
                new Intent(this, EncryptActivity.class),
                new Intent(this, FragmentPagerActivity.class),
                new Intent(this, TouchEventActivity.class),
                new Intent(this, DragWindowActivity.class),
                new Intent(this, ScrollpickerActivity.class),
                new Intent(this, NexusTestActivity.class),
                new Intent(this, EncodeDecodeActivity.class),
                new Intent(this, SendActivity.class),
                new Intent(this, ReceiveActivity.class),
                new Intent(this, QRCodeScanActivity.class),
                new Intent(this, CameraAlbumActivity.class),
                new Intent(this, GraphicLockActivity.class),
                new Intent(this, SocketIOActivity.class),
                new Intent(this, SlidrActivity.class),
                new Intent(this, HotfixActivity.class),
                WebActivity.getStartIntent(this, "hello", "file:///android_asset/www/default.html"/*"http://m.amap.com/"*//*"http://www.bing.com"*/) };

        List<Map<String, Object>> myData = new ArrayList<>();
        for (int i = 0; i < nameData.length; i++) {
            addItem(myData, nameData[i], intentData[i]);
        }
        setListAdapter(new SimpleAdapter(this, myData,
                android.R.layout.simple_list_item_1, new String[] { "title" },
                new int[] { android.R.id.text1 }));
        getListView().setTextFilterEnabled(true);

        //-------------------------init other tool--------------------
        LogUtils.enableDebug(this, true);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (isInMultiWindowMode() && "RTP receiver".equals(map.get("title"))) {   // 先分屏，再打开 rtp receiver
                // launch this activity in another split window next to the current one
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
            }
        }
        startActivity(intent);
    }
}