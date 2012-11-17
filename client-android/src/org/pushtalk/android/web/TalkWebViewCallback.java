package org.pushtalk.android.web;

import java.lang.ref.WeakReference;

import org.pushtalk.android.utils.DialogUtil;
import org.pushtalk.android.utils.JavascriptCallback;
import org.pushtalk.android.utils.Logger;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

public class TalkWebViewCallback implements JavascriptCallback {
    private static final String TAG = "TalkWebViewCallback";
    private final WeakReference<Activity> mActivity;
    private Handler mCallBackHandler;
    
    public static final int MSG_UPDATE_CHANNEL = 10010;
    public static final int MSG_UPDATE_USERNAME = 10011;
    
    public TalkWebViewCallback(Context context) {
        this.mActivity = new WeakReference<Activity>((Activity) context);
    }
    
    public void setCallBackHandler(Handler mCallBackHandler){
        this.mCallBackHandler = mCallBackHandler;
    }

    public void toast(String toast) {
        Logger.d(TAG, "toast - " + toast);
        DialogUtil.showToast(mActivity.get(), toast);
    }
    
    public void close() {
        Logger.d(TAG, "close");
        mActivity.get().finish();
    }
    
    public void cmd(String command, String param1, String param2, String param3) {
        Logger.d(TAG, "Command:" + command + " - param1:" + param1 + ", param2:" + param2 + ", param3:" + param3);
        
        if (null == mActivity || null == mActivity.get()) {
            return;
        }
    }
    
    public void channelUpdated() {
        Logger.d(TAG, "channelUpdated");
        if (null != mCallBackHandler) {
            mCallBackHandler.sendEmptyMessage(MSG_UPDATE_CHANNEL);
        }
    }
    
    public void usernameUpdated() {
        Logger.d(TAG, "usernameUpdated");
        if (null != mCallBackHandler) {
            mCallBackHandler.sendEmptyMessage(MSG_UPDATE_USERNAME);
        }
    }
    

}
