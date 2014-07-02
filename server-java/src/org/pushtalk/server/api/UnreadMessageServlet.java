package org.pushtalk.server.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.pushtalk.server.model.User;
import org.pushtalk.server.utils.ServiceUtils;
import org.pushtalk.server.web.common.NormalBaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnreadMessageServlet extends NormalBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = LoggerFactory.getLogger(UnreadMessageServlet.class);
    

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LOG.debug("api - unread message");
        
        String udid = request.getParameter("udid");
        if (null == udid) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "udid is required!");
            return;
        }
        
        User user = talkService.getUserByUdid(udid);
        if (null == user) {
            LOG.warn("the udid is not registered!");
            return;
        }
        
        String friend = request.getParameter("friend");
        String channelName = request.getParameter("channel_name");
        
        if (StringUtils.isEmpty(friend) && StringUtils.isEmpty(channelName)) {
            LOG.warn("friend/channel is required!");
            return;
        }
        
        String chatting = null;
        if (!StringUtils.isEmpty(channelName)) {
            chatting = ServiceUtils.getChattingChannel(channelName);
            
        } else {
            chatting = ServiceUtils.getChattingFriend(friend);
        }
        
        talkService.unreadMessage(udid, chatting);
        LOG.debug("API unread chatting - " + chatting);
        
        response.getOutputStream().write("OK".getBytes());
    }

	
}


