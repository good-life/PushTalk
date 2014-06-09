package org.pushtalk.server.utils;

import org.apache.log4j.Logger;
import org.pushtalk.server.Config;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JPushService
{
    private static Logger LOG = Logger.getLogger(JPushService.class);

    // Your jpush app_key.
    private static String APP_KEY = Config.JPUSH_APPKEY;
    // Your jpush master_key.
    private static String MASTER_KEY = Config.JPUSH_MASTER_SECRET;
    // This is for ios platform only. true for production enviroment. false for
    // developer enviroment.
    private static boolean apnsProductionOrNot = false;
    // Offline msg's time to live. 0~864000(10 days)
    private static long timeToLive = 864000;

    public static PushResult sendMsgTo(String msg, String alias)
    {

        JPushClient jpushClient = new JPushClient(MASTER_KEY, APP_KEY);

        PushPayload payload = PushPayload
                .newBuilder()
                .setPlatform(Platform.all())
                .setOptions(Options.newBuilder().setApnsProduction(apnsProductionOrNot).setTimeToLive(timeToLive).build())
                .setAudience(Audience.all())
                .setNotification(
                        Notification
                                .newBuilder()
                                .addPlatformNotification(AndroidNotification.newBuilder().setAlert(msg).build())
                                .addPlatformNotification(
                                        IosNotification.newBuilder().setAlert(msg).setBadge(1).setSound("default").addExtra("hello", "123").setContentAvailable(false).build()).build())
                .build();
        LOG.info("Paylaod JSON - " + payload.toString());

        PushResult result = jpushClient.sendPush(payload);
        if (result.isResultOK())
        {
            LOG.debug(result.toString());
        } else
        {
            if (result.getErrorCode() > 0)
            {
                LOG.warn(result.getOriginalContent());
            } else
            {
                LOG.debug("Maybe connect error. Retry laster. ");
            }
        }
        return result;
    }

    public static PushResult sendNotifTo(String msg, String alias)
    {

        JPushClient jpushClient = new JPushClient(MASTER_KEY, APP_KEY);

        PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias)).setNotification(Notification.alert(msg))
                .build();
        LOG.info("Paylaod JSON - " + payload.toString());

        PushResult result = jpushClient.sendPush(payload);
        if (result.isResultOK())
        {
            LOG.debug(result.toString());
        } else
        {
            if (result.getErrorCode() > 0)
            {
                LOG.warn(result.getOriginalContent());
            } else
            {
                LOG.debug("Maybe connect error. Retry laster. ");
            }
        }
        return result;
    }

}
