package com.keven.camera;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.keven.R;
import com.keven.utils.Encrypt;
import com.keven.utils.PermissionUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CameraAlbumActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "CameraAlbum";

    public static final int TAKE_PHOTO = 1;//拍照
    public static final int CHOOSE_PHOTO = 2;//选择相册
    public static final int CROP_PHOTO = 3;//剪切图片

    private String cachPath;
    private File cacheFile;
    private File cameraFile;
    private Uri imageUri;
    private String timestampJpg;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA);

    @BindView(R.id.album_grid)
    GridView gridView;

    private MyGridViewAdpter adpter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_album);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initData() {
        adpter = new MyGridViewAdpter(this, new ArrayList<Image>());
        gridView.setAdapter(adpter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_camera:
                takePhotoForCamera();
                break;
            case R.id.button_album:
                takePhotoForAlbum();
                break;
        }
    }

    private void takePhotoForCamera() {
        //根据当前时间生成图片的名称
        try {
            timestampJpg = Encrypt.MD5Encode(formatter.format(new Date().getTime())) + ".jpg";
        } catch (Exception e) {
            e.printStackTrace();
        }
        cachPath = getDiskCacheDir(CameraAlbumActivity.this) + "/" + timestampJpg;
        cacheFile = getCacheFile(new File(getDiskCacheDir(CameraAlbumActivity.this)), timestampJpg);
        Log.d(TAG, "cachPath = " + cachPath + ",  cacheFile = " + cacheFile);

        PermissionUtil.requestPermission(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new PermissionUtil.PermissionListener() {
                    public void onGranted() {
                        openCamera();
                    }
                },
                "请在设置中打开拍照和读写权限");
    }

    private void openCamera() {
        cameraFile = getCacheFile(new File(getDiskCacheDir(this)), "camera_" + timestampJpg);
        Log.d(TAG, "cameraFile = " + cameraFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imageUri = Uri.fromFile(cameraFile);
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageUri = FileProvider.getUriForFile(CameraAlbumActivity.this, "com.keven.fileprovider", cameraFile);
        }
        // 启动相机程序
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("camerasensortype", 2); // 调用前置摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void takePhotoForAlbum() {
        //根据当前时间生成图片的名称
        try {
            timestampJpg = Encrypt.MD5Encode(formatter.format(new Date().getTime())) + ".jpg";
        } catch (Exception e) {
            e.printStackTrace();
        }
        cachPath = getDiskCacheDir(CameraAlbumActivity.this) + "/" + timestampJpg;
        cacheFile = getCacheFile(new File(getDiskCacheDir(CameraAlbumActivity.this)), timestampJpg);
        Log.d(TAG, "cachPath = " + cachPath + ",  cacheFile = " + cacheFile);

        PermissionUtil.requestPermission(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                new PermissionUtil.PermissionListener() {
                    public void onGranted() {
                        openAlbum();
                    }
                },
                "请在设置中打开读写权限");
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        startPhotoZoom(cameraFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case CROP_PHOTO:
                try {
                    if (resultCode == RESULT_OK) {
                        Image image = new Image();
                        image.setId(String.valueOf(new Date().getTime()));
                        image.setUrl(cachPath);
                        Log.e(TAG, "image Url cachPath =" + cachPath);
                        adpter.notifyDataSetChanged(image);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private File getCacheFile(File parent, String child) {
        // 创建File对象，用于存储拍照后的图片
        File file = new File(parent, child);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = uriToPath(uri);
        Log.i(TAG, "handleImageOnKitKat  file://" + imagePath + "   选择图片的URI=" + uri);
        startPhotoZoom(new File(imagePath));
    }

    private String uriToPath(Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            path = uri.getPath();
        }
        return path;
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        Log.i(TAG, "handleImageBeforeKitKat  file://" + imagePath + "   选择图片的URI=" + uri);
        startPhotoZoom(new File(imagePath));
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 剪裁图片
     */
    private void startPhotoZoom(File file) {
        Log.i(TAG, getImageContentUri(this, file) + "裁剪照片的真实地址");
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(this, file), "image/*");//使用Content Uri替换File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 3);
            intent.putExtra("aspectY", 4);
            intent.putExtra("outputX", MyGridViewAdpter.ITEM_WIDTH);
            intent.putExtra("outputY", MyGridViewAdpter.ITEM_HEIGHT);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            Uri uri = Uri.fromFile(cacheFile);
            Log.e(TAG, "startPhotoZoom  cacheFile =" + cacheFile + "  uri =" + uri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, CROP_PHOTO);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}