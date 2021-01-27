package com.xmcMediacodec.sendencode;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.xmcMediacodec.rtp.RtpSenderWrapper;
import com.xmcMediacodec.androidencode.AvcEncoder;
import com.xmcMediacodec.receivedecode.R;

/*
 * 参考 https://github.com/xmc1715499699/MediaCodec_rtp_send
 */
public class SendActivity extends Activity implements Callback, PreviewCallback {

    DatagramSocket socket;
    InetAddress address;

    AvcEncoder avcCodec;
    public Camera m_camera;
    SurfaceView m_prevewview;
    SurfaceHolder m_surfaceHolder;
    //屏幕分辨率，每个机型不一样，机器连上adb后输入wm size可获取，目前以camera为准
    int width = 1920;
    int height = 1080;
    int framerate = 30;//每秒帧率
    int bitrate = 2500000;//编码比特率，
    private RtpSenderWrapper mRtpSenderWrapper;

    byte[] h264 = new byte[width * height * 3];

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("xmc", "MainActivity__onCreate");
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectAll()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_activity_main);

        //创建rtp并填写需要发送数据流的地址，直播中需要动态获取客户主动请求的地址
        mRtpSenderWrapper = new RtpSenderWrapper(/*NetInfoUtil.getIP(this)*/"192.168.31.36", 5004, false);
        avcCodec = new AvcEncoder(width, height, framerate, bitrate);

        m_prevewview = (SurfaceView) findViewById(R.id.SurfaceViewPlay);
        m_surfaceHolder = m_prevewview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
        m_surfaceHolder.setFixedSize(height, width); // 预览大小設置，camera 是横向，屏幕是竖向
        m_surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        m_surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.v("xmc", "MainActivity+surfaceCreated");
        try {
            m_camera = Camera.open();
            m_camera.setPreviewDisplay(m_surfaceHolder);
            Camera.Parameters parameters = m_camera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
            for (int i = 0; i < sizes.size(); i++) {
                Camera.Size size = sizes.get(i);
                Log.v("xmc", "Camera>>  "+size.width+"   "+size.height);
            }
            parameters.setPreviewSize(width, height);
            parameters.setPictureSize(width, height);
            parameters.setPreviewFormat(ImageFormat.YV12);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            m_camera.setParameters(parameters);
            CameraUtil.setCameraDisplayOrientation(this, Camera.CameraInfo.CAMERA_FACING_BACK, m_camera);
            m_camera.setPreviewCallback(this);
            m_camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.v("xmc", "MainActivity+surfaceDestroyed");
        m_camera.setPreviewCallback(null);  //！！这个必须在前，不然退出出错
        m_camera.release();
        m_camera = null;
        avcCodec.close();
        mRtpSenderWrapper.close();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.v("xmc", "MainActivity+h264 start");
        int ret = avcCodec.offerEncoder(data, h264);
        if (ret > 0) {
            //实时发送数据流
            mRtpSenderWrapper.sendAvcPacket(h264, 0, ret, 0);
        }
        Log.v("xmc", "MainActivity+h264 end");
        Log.v("xmc", "-----------------------------------------------------------------------");
    }
}

/* 自己手机后置摄像头
    Camera>>  4032   3024
    Camera>>  4032   2016
    Camera>>  4000   2250
    Camera>>  3840   2160
    Camera>>  3264   2448
    Camera>>  3264   1836
    Camera>>  3264   1632
    Camera>>  3200   2400
    Camera>>  3008   3008
    Camera>>  2592   1944
    Camera>>  2432   2432
    Camera>>  2160   1080
    Camera>>  1920   1080
    Camera>>  1600   1200
    Camera>>  1440   1080
    Camera>>  1440   720
    Camera>>  1280   960
    Camera>>  1280   768
    Camera>>  1280   720
    Camera>>  1024   768
    Camera>>  800   480
    Camera>>  720   480
    Camera>>  640   640
    Camera>>  640   480
    Camera>>  352   288
    Camera>>  320   240
    Camera>>  176   144
 */