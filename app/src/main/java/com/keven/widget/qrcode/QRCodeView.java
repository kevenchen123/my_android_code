package com.keven.widget.qrcode;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import cn.bingoogolapple.qrcode.core.CameraPreview;
import cn.bingoogolapple.qrcode.core.ProcessDataTask;


public abstract class QRCodeView extends FrameLayout implements PreviewCallback, ProcessDataTask.Delegate {

    protected Camera mCamera;
    protected CameraPreview mPreview;
    protected ScanBoxView mScanBoxView;
    protected QRCodeView.Delegate mDelegate;
    protected Handler mHandler;
    protected boolean mSpotAble;
    protected ProcessDataTask mProcessDataTask;
    private Runnable mOneShotPreviewCallbackTask;

    public QRCodeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QRCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mSpotAble = false;
        this.mOneShotPreviewCallbackTask = new Runnable() {
            public void run() {
                if (QRCodeView.this.mCamera != null && QRCodeView.this.mSpotAble) {
                    QRCodeView.this.mCamera.setOneShotPreviewCallback(QRCodeView.this);
                }
            }
        };
        this.mHandler = new Handler();
        this.initView(context, attrs);
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
    }

    private void initView(Context context, AttributeSet attrs) {
        this.mPreview = new CameraPreview(this.getContext());
        this.mScanBoxView = new ScanBoxView(this.getContext());
        this.mScanBoxView.initCustomAttrs(context, attrs);
        this.addView(this.mPreview);
        this.addView(this.mScanBoxView);
    }

    public void setDelegate(QRCodeView.Delegate delegate) {
        this.mDelegate = delegate;
    }

    public void showScanRect() {
        if (this.mScanBoxView != null) {
            this.mScanBoxView.setVisibility(VISIBLE);
        }
    }

    public void hiddenScanRect() {
        if (this.mScanBoxView != null) {
            this.mScanBoxView.setVisibility(GONE);
        }
    }

    public void startCamera() {
        this.startCamera(0);
    }

    public void startCamera(int cameraFacing) {
        if (this.mCamera == null) {
            CameraInfo cameraInfo = new CameraInfo();

            for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); ++cameraId) {
                Camera.getCameraInfo(cameraId, cameraInfo);
                if (cameraInfo.facing == cameraFacing) {
                    this.startCameraById(cameraId);
                    break;
                }
            }
        }
    }

    private void startCameraById(int cameraId) {
        try {
            this.mCamera = Camera.open(cameraId);
            this.mPreview.setCamera(this.mCamera);
            this.mPreview.setVisibility(View.VISIBLE);
        } catch (Exception var3) {
            if (this.mDelegate != null) {
                this.mDelegate.onScanQRCodeOpenCameraError();
            }
        }
    }

    public void stopCamera() {
        this.stopSpotAndHiddenRect();
        if (this.mCamera != null) {
            this.mPreview.stopCameraPreview();
            this.mPreview.setCamera((Camera) null);
            this.mPreview.setVisibility(View.GONE);
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    public void startSpot() {
        this.startSpotDelay(1000);
    }

    public void startSpotDelay(int delay) {
        this.mSpotAble = true;
        this.startCamera();
        this.mHandler.removeCallbacks(this.mOneShotPreviewCallbackTask);
        this.mHandler.postDelayed(this.mOneShotPreviewCallbackTask, (long) delay);
    }

    public void stopSpot() {
        this.cancelProcessDataTask();
        this.mSpotAble = false;
        if (this.mCamera != null) {
            this.mCamera.setOneShotPreviewCallback((PreviewCallback) null);
        }

        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mOneShotPreviewCallbackTask);
        }
    }

    public void stopSpotAndHiddenRect() {
        this.stopSpot();
        this.hiddenScanRect();
    }

    public void startSpotAndShowRect() {
        this.startSpot();
        this.showScanRect();
    }

    public void openFlashlight() {
        this.mPreview.openFlashlight();
    }

    public void closeFlashlight() {
        this.mPreview.closeFlashlight();
    }

    public void onDestroy() {
        this.stopCamera();
        this.mHandler = null;
        this.mDelegate = null;
        this.mOneShotPreviewCallbackTask = null;
    }

    protected void cancelProcessDataTask() {
        if (this.mProcessDataTask != null) {
            this.mProcessDataTask.cancelTask();
            this.mProcessDataTask = null;
        }
    }

    public void changeToScanBarcodeStyle() {
        if (!this.mScanBoxView.getIsBarcode()) {
            this.mScanBoxView.setIsBarcode(true);
        }
    }

    public void changeToScanQRCodeStyle() {
        if (this.mScanBoxView.getIsBarcode()) {
            this.mScanBoxView.setIsBarcode(false);
        }
    }

    public boolean getIsScanBarcodeStyle() {
        return this.mScanBoxView.getIsBarcode();
    }

    public void onPreviewFrame(final byte[] data, final Camera camera) {
        if (this.mSpotAble) {
            this.cancelProcessDataTask();
            this.mProcessDataTask = (new ProcessDataTask(camera, data, this) {
                protected void onPostExecute(String result) {
                    if (QRCodeView.this.mSpotAble) {
                        if (QRCodeView.this.mDelegate != null && !TextUtils.isEmpty(result)) {
                            try {
                                QRCodeView.this.mDelegate.onScanQRCodeSuccess(result);
                            } catch (Exception var4) {
                            }
                        } else {
                            try {
                                camera.setOneShotPreviewCallback(QRCodeView.this);
                            } catch (Exception var3) {
                            }
                        }
                    }
                }
            }).perform();
        }
    }

    public interface Delegate {
        void onScanQRCodeSuccess(String var1);
        void onScanQRCodeOpenCameraError();
    }
}