package com.keven.qrcode;

import android.Manifest;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.keven.R;
import com.keven.utils.PermissionUtil;
import com.keven.widget.qrcode.QRCodeView;


public class QRCodeScanActivity extends AppCompatActivity implements QRCodeView.Delegate {

    private QRCodeView mCodeScanningView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scan_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        RequestPermission();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCodeScanningView != null) mCodeScanningView.startSpotAndShowRect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCodeScanningView != null) mCodeScanningView.stopCamera();
    }

    @Override
    public void onStop() {
        if (mCodeScanningView != null) mCodeScanningView.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mCodeScanningView != null) mCodeScanningView.onDestroy();
        super.onDestroy();
    }

    /**
     * 重要权限获取
     */
    public void RequestPermission() {
        PermissionUtil.requestPermission(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                new PermissionUtil.PermissionListener() {
                    public void onGranted() {
                        mCodeScanningView = findViewById(R.id.code_scanning_view);
                        mCodeScanningView.setDelegate(QRCodeScanActivity.this);
                        mCodeScanningView.startCamera();
                        //mCodeScanningView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    }
                    public void onDenied() {
                        Toast.makeText(QRCodeScanActivity.this, "请在设置中打开拍照和读写权限...", Toast.LENGTH_LONG).show();
                    };
                },
                "请在设置中打开拍照和读写权限");
    }

    /**
     * 扫码成功
     * @param result
     */
    @Override
    public void onScanQRCodeSuccess(String result) {
        Toast.makeText(this, "扫码结果："+result, Toast.LENGTH_LONG).show();
        vibrate();
        if (mCodeScanningView != null) mCodeScanningView.startSpotAndShowRect();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "打开相机出错", Toast.LENGTH_LONG).show();
        if (mCodeScanningView != null) mCodeScanningView.startSpotAndShowRect();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}