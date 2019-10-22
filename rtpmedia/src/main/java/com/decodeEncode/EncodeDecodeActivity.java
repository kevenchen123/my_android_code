/*
 * Copyright (C) 2016 Martin Storsjo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.decodeEncode;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.xmcMediacodec.receivedecode.R;

/*
 * https://github.com/mstorsjo/android-decodeencodetest
 * https://android.googlesource.com/platform/cts/+/jb-mr2-release/tests/tests/media/src/android/media/cts/ExtractDecodeEditEncodeMuxTest.java
 */
public class EncodeDecodeActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_main);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.extract_decode_edit_encode_mux) {
            new Thread() {
                public void run() {
                    ExtractDecodeEditEncodeMuxTest test = new ExtractDecodeEditEncodeMuxTest();
                    test.setContext(EncodeDecodeActivity.this);
                    try {
                        test.testExtractDecodeEditEncodeMuxAudioVideo();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }.start();
        } else if (i == R.id.decode_edit_encode) {
            new Thread() {
                public void run() {
                    DecodeEditEncodeTest test = new DecodeEditEncodeTest();
                    test.setContext(EncodeDecodeActivity.this);
                    try {
                        test.testVideoEdit720p();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }.start();
        } else if (i == R.id.encode_decode) {
            new Thread() {
                public void run() {
                    EncodeDecodeTest test = new EncodeDecodeTest();
                    test.setContext(EncodeDecodeActivity.this);
                    try {
                        test.testEncodeDecodeVideoFromBufferToBuffer720p();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
