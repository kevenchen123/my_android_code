package com.xmcMediacodec.receivedecode;

import android.app.Activity;
import android.os.Bundle;

/*
 * 参考 https://github.com/xmc1715499699/MediaCodec_rtp_receive
 */
public class ReceiveActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_activity_main);
    }
}