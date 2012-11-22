package org.pushtalk.android.service;

import java.util.zip.Adler32;

import org.pushtalk.android.Config;
import org.pushtalk.android.Constants;
import org.pushtalk.android.R;
import org.pushtalk.android.activity.MainActivity;
import org.pushtalk.android.utils.StringUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class NotificationHelper {

    @SuppressWarnings("deprecation")
    public static void showMessageNotification(
    		Context context, NotificationManager nm, 
    		String title, String msgContent, String channel) {

        boolean isChannel = !StringUtils.isEmpty(channel);
        String chatting = null;
        if (isChannel) {
            chatting = channel;
        } else {
            chatting = title;
        }

        Intent select = new Intent();
        select.setClass(context, MainActivity.class);
        select.putExtra(Constants.KEY_CHATTING, chatting);
        select.putExtra(Constants.KEY_IS_CHANNEL, isChannel);
        select.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        Notification notification = new Notification(R.drawable.ic_launcher, msgContent, System.currentTimeMillis());
        if (!StringUtils.isEmpty(channel)) {
            title = title + " (" + channel + ")";
        }
        
        int notificationId = getNofiticationID(title);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, select, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, title, msgContent, contentIntent);
        
        showMessageNotificationLocal(context, nm, notification, notificationId);
    }

    private static void showMessageNotificationLocal(
    		Context context, NotificationManager nm, Notification notification, int notificationId) {
        notification.ledARGB = 0xff00ff00;
        notification.ledOnMS = 300;
        notification.ledOffMS = 1000;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        boolean needSound = Config.NOTIFICATION_NEED_SOUND;
        boolean needVibrate = Config.NOTIFICATION_NEED_VIBRATE;
        
    	if (needSound && needVibrate) {
        	notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        } else if (needSound){
        	notification.defaults = Notification.DEFAULT_SOUND;
        } else if (needVibrate) {
        	notification.defaults = Notification.DEFAULT_VIBRATE;
        }
    	
        nm.notify(notificationId, notification);
    }
    
    public static int getNofiticationID(String friend) {
        if (TextUtils.isEmpty(friend)) {
            return 0;
        }
        
        int nId = 0;
        Adler32 adler32 = new Adler32();
        adler32.update(friend.getBytes());
        nId = (int) adler32.getValue();
        if (nId < 0) {
            nId = Math.abs(nId);
        }
        
        if (nId < 0) {
            nId = Math.abs(nId);
        }
        return nId;
    }


}
