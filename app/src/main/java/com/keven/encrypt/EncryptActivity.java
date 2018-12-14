package com.keven.encrypt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import com.keven.R;
import com.keven.security.RSACipherStrategy;
import com.keven.security.util.FileUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EncryptActivity extends AppCompatActivity {

    @BindView(R.id.input)EditText ed;
    @BindView(R.id.encrypt)TextView t1;
    @BindView(R.id.decrypt)TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                encrypt(ed.getText().toString());
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void encrypt(String data) {
        Log.d("TAG", "params : " + data);
        // rsa 公钥加密
        RSACipherStrategy rsaCipherStrategy = new RSACipherStrategy();
        try {
            rsaCipherStrategy.initPublicKey(
                    FileUtils.readString(getResources().getAssets().open("rsa_public_key.pem")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String rsaEncrypt = rsaCipherStrategy.encrypt(data);
        Log.d("TAG", "params : " + rsaEncrypt);
        t1.setText(rsaEncrypt);
        // rsa 私钥解密
        try {
            rsaCipherStrategy.initPrivateKey(
                    FileUtils.readString(getResources().getAssets().open("pkcs8_rsa_private_key.pem")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String rsaDecrypt = rsaCipherStrategy.decrypt(rsaEncrypt);
        Log.d("TAG", "params : " + rsaDecrypt);
        t2.setText(rsaDecrypt);
    }
}