package com.keven.socket;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException;

import com.keven.R;
import com.keven.databinding.ActivitySocketioBinding;
import com.keven.utils.JavaUtils;

import org.json.JSONObject;

public class SocketIOActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SocketIOActivity";

    private ActivitySocketioBinding binding;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_socketio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            mSocket = IO.socket("http://socket-io-of-keven.herokuapp.com/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onTimeout);

        //mSocket.on("time", onTime);
        mSocket.on("client-connected", onClientConnect);
        mSocket.on("system", onSystem);
        mSocket.on("other", onOther);

        mSocket.connect();
        if (mSocket.connected()) {
            Toast.makeText(this, "连接服务成功" + mSocket.id(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket = null;
        }
    }

    //--------------------------------------------------------------

    private Emitter.Listener onConnect = (Object... args) -> {
        runOnUiThread(() -> {
            Log.d(TAG, "onConnect >>");
            for (Object obj : args) {
                Log.d(TAG, ">>" + obj);
            }
        });
    };

    private Emitter.Listener onDisconnect = (Object... args) -> {
        runOnUiThread(() -> {
            Log.d(TAG, "onDisconnect >>");
            for (Object obj : args) {
                Log.d(TAG, ">>" + obj);
            }
        });
    };

    private Emitter.Listener onError = (Object... args) -> {
        runOnUiThread(() -> {
            Log.d(TAG, "onError >>");
            for (Object obj : args) {
                Log.d(TAG, ">>" + obj);
            }
        });
    };

    private Emitter.Listener onTimeout = (Object... args) -> {
        runOnUiThread(() -> {
            Log.d(TAG, "onTimeout >>");
            for (Object obj : args) {
                Log.d(TAG, ">>" + obj);
            }
        });
    };

    private Emitter.Listener onTime = (Object... args) -> {
        runOnUiThread(() -> {
            Log.d(TAG, "onTime >>");
            for (Object obj : args) {
                Log.d(TAG, ">>" + obj);
            }
        });
    };

    private Emitter.Listener onClientConnect = (Object... args) -> {
        runOnUiThread(() -> {
            Log.d(TAG, "onClientConnect >>");
            for (Object obj : args) {
                Log.d(TAG, ">>" + obj);
                mSocket.emit("client-join", MessageJson.Builder.newInstance()
                        .put("type", "join")
                        .put("name", JavaUtils.getAndroidID(SocketIOActivity.this))
                        .put("room", "100")
                        .build());
            }
        });
    };

    private Emitter.Listener onSystem = (Object... args) -> {
        runOnUiThread(() -> {
            Log.d(TAG, "onSystem >>");
            for (Object obj : args) {
                Log.d(TAG, ">>" + obj);
            }
        });
    };

    private Emitter.Listener onOther = (Object... args) -> {
        runOnUiThread(() -> {
            Log.d(TAG, "onOther >>");
            for (Object obj : args) {
                Log.d(TAG, ">>" + obj);
            }
        });
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String s = binding.text.getText().toString();
                if (!TextUtils.isEmpty(s) && mSocket != null) {
                    mSocket.emit("user", MessageJson.Builder.newInstance()
                            .put("name", JavaUtils.getAndroidID(SocketIOActivity.this))
                            .put("message", s)
                            .build());
                }
                break;
        }
    }
}