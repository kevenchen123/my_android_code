package com.keven.api;

import android.Manifest;
import android.app.PendingIntent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.keven.retrofit.model.POIbean;
import com.keven.utils.LogUtils;
import com.keven.utils.SystemPropertiesProxy;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import rx.functions.Action1;

import com.keven.R;
import com.keven.retrofit.model.UserResponse;
import com.keven.retrofit.subscribers.ProgressSubscriber;
import com.keven.retrofit.subscribers.SubscriberOnNextListener;
import com.keven.utils.AndroidComponentUtil;
import com.keven.utils.SchedulerProvider;
import com.keven.retrofit.DataService;
import com.keven.notification.NotificationHandler;
import com.keven.databinding.ActivityAndroidApiBinding;
import com.cwj.fataar.Test;


public class AndroidApiActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAndroidApiBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_android_api);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testNotification();
        testAMS();
        setClick();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //--------------------------------------------------------------

    private ArrayList<AndroidComponentUtil.RecentTag> mTasks;

    private void testAMS() {
        mTasks = AndroidComponentUtil.getRecentTask(this, 6);
        AndroidComponentUtil.getRunningTask(this);
    }

    private void removeTask() {
        if (mTasks.size() > 0 && mTasks.get(0) != null) {
            AndroidComponentUtil.removeTask(this, mTasks.get(0).info.id);
            mTasks.remove(0);
        }
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

    private void setClick() {
        //有点 ： 防暴力点击的
        RxView.clicks(findViewById(R.id.button_fataar))
                .throttleFirst(3000, TimeUnit.MILLISECONDS) // 设置防抖间隔为 3000ms
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        testFatAar();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.simple_notification:
                nHandler.createRemoteViewNotification(this, this.getPackageName());
                break;
            case R.id.big_notification:
                nHandler.createExpandableNotification(this);
                break;
            case R.id.progress_notification:
                nHandler.createProgressNotification(this);
                break;
            case R.id.button_notifcation:
                nHandler.createButtonNotification(this);
                break;
            case R.id.remove_task:
                removeTask();
                break;
            case R.id.button_sms:
                testSms();
                break;
            case R.id.button_retrofit:
                testRetrofit();
                break;
            case R.id.button_activity_info:
                testFilterActivity();
                break;
            case R.id.button_system_property:
                testSystemProperty();
                break;
        }
    }

    //--------------------------------------------------------------

    private void testSms() {
        new RxPermissions(this).request(
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        PendingIntent sentPI = null;
                        PendingIntent deliverPI = null;
                        // 获取短信管理器
                        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                        // 拆分短信内容（手机短信长度限制）
                        List<String> divideContents = smsManager.divideMessage("123abc");
                        for (String text : divideContents) {
                            smsManager.sendTextMessage("13524573505", null, text, sentPI, deliverPI);
                        }
                    }
                });
    }

    //--------------------------------------------------------------

    private void testFatAar() {
        Test.fun(this);
        LogUtils.d("TAG", "********** " + Test.textaa);
        String s =null; s.trim();
    }

    //--------------------------------------------------------------

    private void testRetrofit() {
        /*github api*/
        DataService.getInstance(AndroidApiActivity.this).getUser("kevenchen123")
                .compose(SchedulerProvider.DEFAULTTREADPOOL.applySchedulers())
                .subscribe(
                        new ProgressSubscriber(new SubscriberOnNextListener<UserResponse>() {
                            @Override
                            public void onNext(UserResponse result) {
                                Toast.makeText(AndroidApiActivity.this, result.name + result.id + result.contact, Toast.LENGTH_LONG).show();
                            }
                        }, this));

        DataService.getInstance(AndroidApiActivity.this).getBaidu()
                .compose(SchedulerProvider.DEFAULT.applySchedulers())
                .subscribe(
                        new ProgressSubscriber(new SubscriberOnNextListener<ResponseBody>() {
                            @Override
                            public void onNext(ResponseBody result) {
                                try {
                                    Toast.makeText(AndroidApiActivity.this, result.string(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, this));

        DataService.getInstance(AndroidApiActivity.this).getAmapLocation("上海市闵行区甬虹路69号虹桥绿谷广场G栋")
                .compose(SchedulerProvider.DEFAULTTREADPOOL.<POIbean>applySchedulers())
                .subscribe(new Action1<POIbean>() {
                    @Override
                    public void call(POIbean poIbean) {
                        Log.d("TAG", "POI=" + poIbean);
                    }
                });
    }

    //--------------------------------------------------------------

    private void testFilterActivity() {
        startActivity(
                AndroidComponentUtil.filterActivity(this, getApplicationContext().getPackageName())
                        .get("Slidr"));
    }

    //--------------------------------------------------------------

    private void testSystemProperty() {
        Toast.makeText(AndroidApiActivity.this, SystemPropertiesProxy.get(this, "ro.build.version.release"), Toast.LENGTH_LONG).show();
    }
}