package org.pushtalk.server.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.pushtalk.server.model.Channel;
import org.pushtalk.server.model.Message;
import org.pushtalk.server.utils.ServiceUtils;
import org.pushtalk.server.web.common.FreemarkerBaseServlet;

import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

public class TalkServlet extends FreemarkerBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(TalkServlet.class);

	private static final String JPUSH_USERNAME = "pushtalk";
	private static final String JPUSH_PASSWORD = "654321";
	private static final String JPUSH_APPKEY = "7d431e42dfa6a6d693ac2d04";
	
	private static final JPushClient jpushClient = new JPushClient(JPUSH_USERNAME, JPUSH_PASSWORD, JPUSH_APPKEY);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	    LOG.debug("action - talk");
        Map data = new HashMap();

        String udid = request.getParameter("udid");
        if (null == udid) {
            responseError(response, "udid is required!");
            return;
        }
        data.put("udid", udid);
        LOG.debug("My udid: " + udid);

        String channelName = request.getParameter("channel_name");
        String friend = request.getParameter("friend");
        if (null == channelName && null == friend) {
            responseError(response, "channelName/friend is required!");
            return;
        }
        
        final String content = request.getParameter("message");
        if (StringUtils.isEmpty(content)) {
            data.put("info", "Message is empty!");
            processTemplate(response, "chatting.html", data);
        }

        String chatting = null;
        MessageResult msgResult = null;
        final String myName = talkService.getUserByUdid(udid).getName();
        if (null != channelName) {
            Channel channel = talkService.getChannelByName(channelName);
            if (null == channel) {
                responseError(response, "Unexpected: the channel does not exist - " + channelName);
                return;
            }
            data.put("channel", channel);
            
            Map<String, Object> extra = new HashMap<String, Object>();
            extra.put("channel", channelName);
            msgResult = jpushClient.sendCustomMessageWithTag(ServiceUtils.postfixAliasAndTag(channelName),
                    myName, content, null, extra);
            chatting = ServiceUtils.getChattingChannel(channelName);

        } else {
            data.put("friend", friend);
            msgResult = jpushClient.sendCustomMessageWithAlias(ServiceUtils.postfixAliasAndTag(friend), 
                    myName, content);
            chatting = ServiceUtils.getChattingChannel(myName, friend);
        }
        
        if (null == msgResult) {
            String info = "Unexpected: null result.";
            LOG.error(info);
            data.put("error", info);
            
        } else if (msgResult.getErrcode() != 0) {
            String info = "Send msg error - errorCode:" + msgResult.getErrcode()
                    + ", errorMsg:" + msgResult.getErrmsg()
                    + ", sendno:" + msgResult.getSendno();
            LOG.error(info);
            data.put("error", info);
            
        } else {
            Message message = new Message(myName, content, channelName);
            talkService.putMessage(udid, chatting, message);
        }
		
		processTemplate(response, "chatting.html", data);
	}
	
}
