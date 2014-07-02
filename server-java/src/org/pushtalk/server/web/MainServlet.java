package org.pushtalk.server.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pushtalk.server.model.RecentChat;
import org.pushtalk.server.model.User;
import org.pushtalk.server.web.common.FreemarkerBaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainServlet extends FreemarkerBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static final Logger LOG = LoggerFactory.getLogger(MainServlet.class);


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	    
	    LOG.debug("action - main");
        Map data = new HashMap();
	    
	    String udid = request.getParameter("udid");
	    if (null == udid) {
	        responseError(response, "udid is required!");
	        return;
	    }
        data.put("udid", udid);
        
        String channelCreated = request.getParameter("channelCreated");
        if (null != channelCreated) {
            data.put("channelCreated", true);
        }
        String channelDeleted = request.getParameter("channelDeleted");
        if (null != channelDeleted) {
            data.put("channelDeleted", true);
        }
        String usernameUpdated = request.getParameter("usernameUpdated");
        if (null != usernameUpdated) {
            data.put("usernameUpdated", true);
        }
        
        
	    
	    User user = talkService.getUserByUdid(udid);
	    if (null == user) {
	        // not registered yet
	        processTemplate(response, "register.html", data);
	        return;
	    }
	    
        Set<RecentChat> recentChats = talkService.getRecentChats(udid);
	    
		data.put("user", user);
		data.put("myChannelList", user.getChannelList());
		data.put("recentChats", recentChats);
		
		processTemplate(response, "main.html", data);
	}

	
}

