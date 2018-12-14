package com.keven.api;

import android.Manifest;
import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.keven.R;
import com.tbruyelle.rxpermissions.RxPermissions;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import java.util.List;

import com.keven.retrofit.DataService;
import com.keven.notification.NotificationHandler;
import com.cwj.fataar.Test;


public class AndroidApiActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_api);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testNotification();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    //--------------------------------------------------------------

    NotificationHandler nHandler;

    private void testNotification() {
        nHandler = NotificationHandler.getInstance(this);

        findViewById(R.id.simple_notification).setOnClickListener(this);
        findViewById(R.id.big_notification).setOnClickListener(this);
        findViewById(R.id.progress_notification).setOnClickListener(this);
        findViewById(R.id.button_notifcation).setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.simple_notification:
                nHandler.createSimpleNotification(this);
                break;
            case R.id.big_notification:
                nHandler.createExpandableNotification(this);
                break;
            case R.id.progress_notification:
                nHandler.createProgressNotification(this);
                break;
            case R.id.button_notifcation:
                nHandler.createButtonNotification(this);
            case R.id.button_sms:
                testSms();
                break;
            case R.id.button_fataar:
                testFatAar();
                break;
            case R.id.button_retrofit:
                testRetrofit();
                break;
        }
    }

    //--------------------------------------------------------------

    private void testSms() {
        new RxPermissions(this).request(
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    PendingIntent sentPI = null;
                    PendingIntent deliverPI = null;
                    // 获取短信管理器
                    android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                    // 拆分短信内容（手机短信长度限制）
                    List<String> divideContents = smsManager.divideMessage("123abc");
                    for (String text : divideContents) {
                        smsManager.sendTextMessage("13524573505", null, text, sentPI, deliverPI);
                    }
                });
    }

    //--------------------------------------------------------------

    private void testFatAar() {
        Test.fun(this);
        Log.d("TAG", "********** "+ Test.textaa );
    }

    //--------------------------------------------------------------

    private void testRetrofit() {
        DataService.getInstance(AndroidApiActivity.this).login("hello_rsa")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {  },
                        error -> {  });
    }
}