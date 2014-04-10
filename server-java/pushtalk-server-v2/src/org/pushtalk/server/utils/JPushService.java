package org.pushtalk.server.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.pushtalk.server.Config;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.DeviceEnum;
import cn.jpush.api.push.IosExtras;
import cn.jpush.api.push.MessageResult;
import cn.jpush.api.push.NotificationParams;
import cn.jpush.api.push.ReceiverTypeEnum;

public class JPushService
{
    private static Logger LOG = Logger.getLogger(JPushService.class);

    // Your jpush app_key.
    private static String APP_KEY = Config.JPUSH_APPKEY;
    // Your jpush master_key.
    private static String MASTER_KEY = Config.JPUSH_MASTER_SECRET;
    // Your platform. (DeviceEnum.IOS for ios platform. DeviceEnum.Android for
    // android platform. Null for all platforms.)
    private static DeviceEnum device = null;
    // This is for ios platform only. true for production enviroment. false for
    // developer enviroment.
    private static boolean apnsProductionOrNot = false;
    // Offline msg's time to live. 0~864000(10 days)
    private static long timeToLive = 864000;

    public static void sendMsgTo(String msg, String alias)
    {

        JPushClient jpushClient = new JPushClient(MASTER_KEY, APP_KEY, timeToLive, device, apnsProductionOrNot);
        NotificationParams nps = new NotificationParams();
        nps.setReceiverType(ReceiverTypeEnum.ALIAS);
        nps.setReceiverValue(alias);
        IosExtras extras = new IosExtras(1, "Default");
        extras.setBadge(1);
        extras.setSound("Default");
        Map<String, Object> e = new HashMap<String, Object>();
        e.put("ios", extras);

        MessageResult msgResult = jpushClient.sendNotification(msg, nps, e);
        LOG.debug("responseContent - " + msgResult.responseResult.responseContent);
        if (msgResult.isResultOK())
        {
            LOG.info("msgResult - " + msgResult);
            LOG.info("messageId - " + msgResult.getMessageId());
        } else
        {
            if (msgResult.getErrorCode() > 0)
            {
                // 业务异常
                LOG.warn("Service error - ErrorCode: " + msgResult.getErrorCode() + ", ErrorMessage: " + msgResult.getErrorMessage());
            } else
            {
                // 未到达 JPush
                LOG.error("Other excepitons - " + msgResult.responseResult.exceptionString);
            }
        }
    }

}
