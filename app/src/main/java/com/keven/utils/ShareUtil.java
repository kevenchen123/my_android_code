package com.keven.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * @author _H_JY
 * 2015-8-27下午4:26:35
 * <p/>
 * 分享工具类：可以分享到微信好友、微信收藏、微信朋友圈、QQ好友、QQ空间、短信
 */
public class ShareUtil {

    public static final String WX_APP_ID = "";//改成你在微信开放平台审核通过的appID
    public static final String QQ_APP_ID = "";//改成你在QQ开放平台审核通过的appID

    private Tencent tencent;
    private IWXAPI iwxapi;

    public ShareUtil() {
        super();
    }

    /**
     * 要分享必须先注册(微信)
     */
    public void regToWX(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, WX_APP_ID, true);
        iwxapi.registerApp(WX_APP_ID);
    }

    public IWXAPI getIwxapi() {
        return iwxapi;
    }

    public void setIwxapi(IWXAPI iwxapi) {
        this.iwxapi = iwxapi;
    }

    /**
     * 要分享必须先注册(QQ)
     */
    public void regToQQ(Context context) {
        tencent = Tencent.createInstance(QQ_APP_ID, context);
    }

    public Tencent getTencent() {
        return tencent;
    }

    public void setTencent(Tencent tencent) {
        this.tencent = tencent;
    }

    public String getWxAppId() {
        return WX_APP_ID;
    }

    public String getQqAppId() {
        return QQ_APP_ID;
    }


    /**
     * 分享到短信
     */
    public Intent sendSMS(String description) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
        //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
        sendIntent.putExtra("sms_body", description);
        sendIntent.setType("vnd.android-dir/mms-sms");
        return sendIntent;
    }

    public void sendDX(String webUrl) {
        String smsBody = "我正在浏览这个,觉得真不错,推荐给你哦~ 地址:" + webUrl;
        Uri smsToUri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
        //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
        //短信内容
        sendIntent.putExtra("sms_body", smsBody);
        sendIntent.setType("vnd.android-dir/mms-sms");
    }


    /**
     * 分享到微信好友
     */
    public void shareToWXSceneSession(String url, String shareTitle, String description) {

        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        WXMediaMessage mWxMediaMessage = new WXMediaMessage(webpageObject);
        mWxMediaMessage.title = shareTitle;
        mWxMediaMessage.description = description;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());//transaction字段用于唯一标识一个请求
        req.message = mWxMediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }


    /**
     * 分享到微信收藏
     */
    public void shareToWXSceneFavorite(String url, String shareTitle, String description) {

        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        WXMediaMessage wxMediaMessage = new WXMediaMessage(webpageObject);
        wxMediaMessage.title = shareTitle;
        wxMediaMessage.description = description;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = wxMediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneFavorite;
        iwxapi.sendReq(req);
    }


    /**
     * 分享到微信朋友圈
     */
    public void shareToWXSceneTimeline(String url, String shareTitle, String description, String imageUrl) {

        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        WXMediaMessage mediaMessage = new WXMediaMessage(webpageObject);
        mediaMessage.title = shareTitle;
        mediaMessage.description = description;
        Bitmap bitmap = BitmapFactory.decodeFile(imageUrl);
        Bitmap thumBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);//图片大小有限制，太大分享不了
        mediaMessage.thumbData = new PictureUtils().Bitmap2Bytes(thumBmp);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = mediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }


    /**
     * 分享到QQ好友
     */
    public void shareToQQ(Activity activity, String url, String shareTitle, String description, IUiListener uiListener) {

        Bundle qqParams = new Bundle();
        qqParams.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        qqParams.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
        qqParams.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        qqParams.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        //qqParams.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "APP名称");
        tencent.shareToQQ(activity, qqParams, uiListener);
    }


    /**
     * 分享到QQ空间
     */
    public void shareToQzone(Activity activity, String url, String imageUrl, String shareTitle, String description, IUiListener uiListener) {

        Bundle qzoneParams = new Bundle();
        qzoneParams.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        qzoneParams.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareTitle);//必填
        qzoneParams.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, description);
        qzoneParams.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
        ArrayList<String> imageUrlList = new ArrayList<String>();
        imageUrlList.add(imageUrl);
        qzoneParams.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrlList);
        tencent.shareToQzone(activity, qzoneParams, uiListener);
    }

    public void doPayWechat(Activity activity, String partnerId, String orderId, String packageValue, String nonceStr, String timeStamp, String sign) {
        iwxapi = WXAPIFactory.createWXAPI(activity, ShareUtil.WX_APP_ID, true);
        iwxapi.registerApp(ShareUtil.WX_APP_ID);
        PayReq req = new PayReq();
        req.appId = WX_APP_ID;
        req.partnerId = partnerId;
        req.prepayId = orderId;
        req.packageValue = packageValue;
        req.nonceStr = nonceStr;
        req.timeStamp = "";
        req.sign = sign;
        iwxapi.sendReq(req);
    }


    /**
     * 检查qq是否安装
     *
     * @return
     */
    private boolean checkQQInstalled() {
        if (AndroidComponentUtil.isInstallPackage("com.tencent.mobileqq")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查微信是否安装
     *
     * @return
     */
    private boolean checkWeChatInstalled() {
        if (AndroidComponentUtil.isInstallPackage("com.tencent.mm")) {
            return true;
        } else {
            return false;
        }
    }

    //QQ和Qzone分享回调
    private class BaseUiListener implements IUiListener {
        @Override
        public void onCancel() {
        }
        @Override
        public void onComplete(Object arg0) {
        }
        @Override
        public void onError(UiError arg0) {
        }
    }
}