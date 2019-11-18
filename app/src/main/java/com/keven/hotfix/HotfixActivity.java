package com.keven.hotfix;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.keven.R;
import com.keven.hotfix.tinker.TinkerManager;
import com.keven.utils.PermissionUtil;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;


public class HotfixActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotfix);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d("HotfixActivity", "onCreate");
        PermissionUtil.requestPermission(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                new PermissionUtil.PermissionListener() {
                    public void onGranted() {
                        Log.d("HotfixActivity", "onGranted");
                        findViewById(R.id.load_patch).setEnabled(true);
                        findViewById(R.id.clear_patch).setEnabled(true);
                    }
                },
                "请在设置中打开读写权限");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restart_app:
                ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }
    }

    public void loadPatch(View view) {
        TinkerManager.loadPatch();
    }

    public void clearPatch(View view) {
        TinkerManager.clearPatch();
    }

    public void showInfo(View view) {
        TinkerManager.showInfo(this);
    }
}