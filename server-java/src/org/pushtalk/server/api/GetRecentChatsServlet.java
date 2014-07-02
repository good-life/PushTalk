package org.pushtalk.server.api;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pushtalk.server.model.RecentChat;
import org.pushtalk.server.model.User;
import org.pushtalk.server.web.common.NormalBaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetRecentChatsServlet extends NormalBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = LoggerFactory.getLogger(GetRecentChatsServlet.class);
    
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LOG.debug("api - get recent chats");
        
        String udid = request.getParameter("udid");
        if (null == udid) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "udid is required!");
            return;
        }
        LOG.debug("udid - " + udid);
        
        User user = talkService.getUserByUdid(udid);
        if (null == user) {
            response.sendError(HttpServletResponse.SC_NO_CONTENT, "the udid is not registered!");
            return;
        }
        
        Set<RecentChat> recentChats = talkService.getRecentChats(udid);
        
        response.getOutputStream().write(gson.toJson(recentChats).getBytes());   
    }

	
}
