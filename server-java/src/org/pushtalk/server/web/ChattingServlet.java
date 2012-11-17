package org.pushtalk.server.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pushtalk.server.model.Channel;
import org.pushtalk.server.model.User;
import org.pushtalk.server.utils.ServiceUtils;
import org.pushtalk.server.utils.StringUtils;
import org.pushtalk.server.web.common.FreemarkerBaseServlet;

public class ChattingServlet extends FreemarkerBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(ChattingServlet.class);


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
        
        String channelName = request.getParameter("channel_name");
        String friend = request.getParameter("friend");
        if (StringUtils.isTrimedEmpty(channelName) && StringUtils.isTrimedEmpty(friend)) {
            responseError(response, "channel_name/friend is required!");
            return;
        }
        
        if (!StringUtils.isTrimedEmpty(channelName)) {
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
            User user = talkService.getUserByName(friend);
            if (null == user) {
                responseError(response, "Unexpected: the friend does not exist - " + friend);
                return;
            }
            
            data.put("friend", user.getName());
        }
        
        processTemplate(response, "chatting.html", data);
        
	}

	
}
