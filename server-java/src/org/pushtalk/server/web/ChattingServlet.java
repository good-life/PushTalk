package org.pushtalk.server.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pushtalk.server.model.Channel;
import org.pushtalk.server.model.User;
import org.pushtalk.server.utils.ServiceUtils;
import org.pushtalk.server.utils.StringUtils;
import org.pushtalk.server.web.common.FreemarkerBaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChattingServlet extends FreemarkerBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static final Logger LOG = LoggerFactory.getLogger(ChattingServlet.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("action - chatting");
        Map data = new HashMap();
	    
        String udid = request.getParameter("udid");
        if (null == udid) {
            responseError(response, "udid is required!");
            return;
        }
        data.put("udid", udid);

        String chatting = request.getParameter("chatting");
        if (StringUtils.isTrimedEmpty(chatting)) {
            responseError(response, "chatting (target) is required!");
            return;
        }
        
        String isChannelStr = request.getParameter("isChannel");
        boolean isChannel = Boolean.valueOf(isChannelStr);
        
        if (isChannel) {
            String channelName = chatting;
            if (channelName.startsWith(ServiceUtils.CHANNEL_PREFIX)) {
                channelName = channelName.substring(1, channelName.length() + 1);
            }
    	    Channel channel = talkService.getChannelByName(channelName);
    	    if (null == channel) {
    	        responseError(response, "Unexpected: the channel does not exist - " + channelName);
    	        return;
    	    }
    	    
    		data.put("channel", channel);
    		
        } else {
            String friend = chatting;
            if (friend.startsWith(ServiceUtils.USER_PREFIX)) {
                friend = friend.substring(1, friend.length() + 1);
            }
            User user = talkService.getUserByName(friend);
            if (null == user) {
                responseError(response, "Unexpected: the friend does not exist - " + friend);
                return;
            }
            
            data.put("friend", user.getName());
        }
        User user = talkService.getUserByUdid(udid);
        data.put("user", user);
        data.put("serverTime", System.currentTimeMillis());
        processTemplate(response, "chatting.html", data);
        
	}

	
}
