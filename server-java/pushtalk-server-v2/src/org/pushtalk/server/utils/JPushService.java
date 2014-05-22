package org.pushtalk.server.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.pushtalk.server.Config;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.DeviceEnum;
import cn.jpush.api.push.CustomMessageParams;
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

    public static MessageResult sendMsgTo(String msg, String alias)
    {

        JPushClient jpushClient = new JPushClient(MASTER_KEY, APP_KEY, timeToLive, device, apnsProductionOrNot);
        CustomMessageParams params = new CustomMessageParams();
        params.setReceiverType(ReceiverTypeEnum.ALIAS);
        params.setReceiverValue(alias);
        
        IosExtras extras = new IosExtras(1, "Default");
        extras.setBadge(1);
        extras.setSound("Default");
        Map<String, Object> e = new HashMap<String, Object>();
//        e.put("ios", extras);
        e.put("pushtalk_message", msg);

        MessageResult msgResult = jpushClient.sendCustomMessage("", "", params, e);
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
        return msgResult;
    }
    
    public static MessageResult sendNotifTo(String msg, String alias)
    {

        JPushClient jpushClient = new JPushClient(MASTER_KEY, APP_KEY, timeToLive, device, apnsProductionOrNot);
        NotificationParams params = new NotificationParams();
        params.setReceiverType(ReceiverTypeEnum.ALIAS);
        params.setReceiverValue(alias);
        
//        Map<String, Object> iose = new HashMap<String, Object>();
//        iose.put("content-available", 1);
//        iose.put("badge", 1);
//        iose.put("sound", "sound.caf");
        
        IosExtras iose = new IosExtras(1,"sound.caf");
        
        Map<String, Object> e = new HashMap<String, Object>();
        e.put("ios", iose);
        e.put("pushtalk_message", msg);

        MessageResult msgResult = jpushClient.sendNotification("", params, e);
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
        return msgResult;
    }

}
