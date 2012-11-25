package org.pushtalk.android;

import org.pushtalk.android.utils.AndroidUtil;
import org.pushtalk.android.utils.Logger;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

public class TalkApplication extends Application {
    private static final String TAG = "TalkApplication";

    @Override
    public void onCreate() {
        Logger.d(TAG, "onCreate");
        super.onCreate();
        
        Logger.i(TAG, "Push Talk client version: " + Config.VERSION);
        Global.init(getApplicationContext());

        Config.udid = AndroidUtil.getUdid(getApplicationContext());
        Logger.d(TAG, "My udid: " + Config.udid);

        JPushInterface.setDebugMode(true); // 设置开启日志
        JPushInterface.init(this); // 初始化 JPush

    }
}
