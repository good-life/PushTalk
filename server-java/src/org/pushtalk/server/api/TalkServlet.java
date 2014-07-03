package org.pushtalk.server.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jpush.api.push.model.notification.Notification;
import org.apache.commons.lang.StringUtils;
import org.pushtalk.server.Config;
import org.pushtalk.server.model.Channel;
import org.pushtalk.server.model.Message;
import org.pushtalk.server.utils.ServiceUtils;
import org.pushtalk.server.web.common.FreemarkerBaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

public class TalkServlet extends FreemarkerBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = LoggerFactory.getLogger(TalkServlet.class);

	private static final JPushClient jpushClient = new JPushClient(
				Config.JPUSH_MASTER_SECRET,Config.JPUSH_APPKEY);

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	    LOG.debug("action - talk");
        Map<String, Object> data = new HashMap<String, Object>();

        String udid = request.getParameter("udid");
        String channelName = request.getParameter("channel_name");
        String friend = request.getParameter("friend");
        String content = request.getParameter("message");
        String chatNo = request.getParameter("chatNo");
        if (null == udid) {
            return;
        }
        if (null == channelName && null == friend) {
            return;
        }
        if (StringUtils.isEmpty(content)) {
            return;
        }
        
        LOG.debug("udid (" + udid +") talk:" + content);
        
        String chatting = null;
        PushResult result = null;
        String myName = talkService.getUserByUdid(udid).getName();

        PushPayload.Builder payload = PushPayload.newBuilder()
                .setPlatform(Platform.android_ios());
        
        if (null != channelName) {
            Channel channel = talkService.getChannelByName(channelName);
            if (null == channel) {
                data.put("error", "the channel does not exist - " + channelName);
            } else {
                //群组聊天
	            Map<String, String> extras = new HashMap<String, String>();
	            extras.put("channel", channelName);
	            payload = payload
                        .setNotification(Notification.ios(myName + " 给您发送了一条信息", extras))
	                    .setAudience(Audience.tag(ServiceUtils.postfixAliasAndTag(channelName)))
	                    .setMessage(cn.jpush.api.push.model.Message.newBuilder()
	                            .setMsgContent(content)
	                            .addExtras(extras)
                                .setTitle(myName)
	                            .build());

	            chatting = ServiceUtils.getChattingChannel(channelName);
            }
        } else {
            //个人聊天
        	payload = payload
                    .setNotification(Notification.ios(myName + " 给您发送了一条信息", new HashMap<String, String>()))
        	        .setAudience(Audience.alias(ServiceUtils.postfixAliasAndTag(friend)))
                    .setMessage(cn.jpush.api.push.model.Message.newBuilder()
                            .setMsgContent(content)
                            .setTitle(myName)
                            .addExtras(new HashMap<String, String>())
                            .build());
            
            chatting = ServiceUtils.getChattingChannel(myName, friend);
        }
        
        try {
            result = jpushClient.sendPush(payload.build());
            
            Message message = new Message(result.sendno, myName, content, channelName);
            talkService.putMessage(udid, chatting, message);
            
            if (null != friend) {
                chatting = ServiceUtils.getChattingFriend(friend);
            }
            talkService.newRecentChat(udid, chatting);
            talkService.showedMessage(udid, chatting);
            
            data.put("sent", true);
            data.put("message", message);
            data.put("chatNo", chatNo);
            
        } catch (APIConnectionException e) {
            // TODO: need retry
            
        } catch (APIRequestException e) {
            String info = "Send msg error - errorCode:" + e.getErrorCode()
                    + ", errorMsg:" + e.getErrorMessage();
            LOG.error(info);
            data.put("error", info);
        }
        
        processJSON(response, data);
	}
	
	
    public static final int MAX = Integer.MAX_VALUE / 2;
    public static final int MIN = MAX / 2;
    
    /**
     * 保持 sendNo 的唯一性是有必要的
     * It is very important to keep sendNo unique.
     * @return sendNo
     */
    public static int getRandomSendNo() {
        return (int) (MIN + Math.random() * (MAX - MIN));
    }
    
}
