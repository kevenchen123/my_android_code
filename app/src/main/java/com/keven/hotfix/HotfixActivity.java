package com.keven.hotfix;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.keven.R;
import com.keven.utils.PermissionUtil;

import java.io.File;


public class HotfixActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FILE_END = ".apk";//文件后缀
    private String FILEDIR;//文件路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotfix);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PermissionUtil.requestPermission(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                new PermissionUtil.PermissionListener() {
                    public void onGranted() {
                        //    /storage/emulated/0/tpatch/
                        FILEDIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tpatch/";
                        //创建路径对应的文件夹
                        File file = new File(FILEDIR);
                        if (!file.exists()) file.mkdir();
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
            case R.id.load_andfix:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }
    }

    public void loadPatch(View view) {
        TinkerManager.loadPatch(getPatchName());
    }

    public String getPatchName() {
        return FILEDIR.concat("tinker").concat(FILE_END);
    }
}