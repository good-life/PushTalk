package org.pushtalk.android.service;

import org.json.JSONObject;
import org.pushtalk.android.Config;
import org.pushtalk.android.activity.WebPageActivity;
import org.pushtalk.android.utils.AndroidUtil;
import org.pushtalk.android.utils.Logger;
import org.pushtalk.android.utils.StringUtils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

public class TalkReceiver extends BroadcastReceiver {
    private static final String TAG = "TalkReceiver";
    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        
        Bundle bundle = intent.getExtras();
        Logger.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + AndroidUtil.printBundle(bundle));
        
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Logger.d(TAG, "接受到推送下来的自定义消息: " + message);
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            
            String channel = null;
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject json = new JSONObject(extras);
                channel = json.optString("channel");
            } catch (Exception e) {
                Logger.w(TAG, "");
            }
            if (!StringUtils.isEmpty(channel)) {
                title = title + " (" + channel + ")";
            }
            
            if (!Config.isBackground) {
                Intent msgIntent = new Intent(WebPageActivity.MESSAGE_RECEIVED_ACTION);
                msgIntent.putExtra("message", message);
                msgIntent.putExtra("title", title);
                if (null != channel) {
                    msgIntent.putExtra("channel", channel);
                }
                context.sendBroadcast(msgIntent);
                Logger.v(TAG, "sending msg to ui ");
            }
            
            if (Config.IS_TEST_MODE) {
                NotificationHelper.showMessageNotification(context, nm, title, message);
            }
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Logger.d(TAG, "接受到推送下来的通知");
            
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Logger.d(TAG, "用户点击打开了通知");
            
        } else {
            Logger.d(TAG, "Unhandled intent - " + intent.getAction());
        }

    }


    
}

