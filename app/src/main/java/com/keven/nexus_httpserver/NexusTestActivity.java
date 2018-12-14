package com.keven.nexus_httpserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.keven.utils.WebImageView;
import com.keven.R;
import com.keven.nanohttpd.NanoHTTPD;
import com.keven.nanohttpd.NetInfoUtil;

import java.io.IOException;

public class NexusTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nexus_httpserver);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showWebImageView();
        startHttpServer();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void showWebImageView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        WebImageView image = (WebImageView) findViewById(R.id.image);
        image.showImageUrl("http://pic1.win4000.com/wallpaper/5/54055707675cb.jpg");
        //image.showLocalImage(R.mipmap.ic_launcher);
    }

    private void startHttpServer() {
        ((TextView)findViewById(R.id.http_server)).setText("Nano Http Server \n" +
                "http://" + NetInfoUtil.getIP(this)  + ":8080");
        NetInfoUtil.copyResourceFile(this, R.raw.index, getExternalCacheDir() + "/index.html");
        NetInfoUtil.copyResourceFile(this, R.raw.style, getExternalCacheDir() + "/style.css");
        NetInfoUtil.copyResourceFile(this, R.raw.resume, getExternalCacheDir() + "/resume.pdf");
        try {
            new NanoHTTPD(8080, getExternalCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}