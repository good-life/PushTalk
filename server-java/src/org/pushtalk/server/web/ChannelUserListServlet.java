package org.pushtalk.server.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pushtalk.server.model.Channel;
import org.pushtalk.server.model.User;
import org.pushtalk.server.web.common.FreemarkerBaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelUserListServlet extends FreemarkerBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static final Logger LOG = LoggerFactory.getLogger(ChannelUserListServlet.class);


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("action - list channel users");
        Map data = new HashMap();
	    
	    String channelName = request.getParameter("channel_name");
        if (null == channelName) {
            responseError(response, "channelName is required!");
            return;
        }
        
        String udid = request.getParameter("udid");
        if (null == udid) {
            responseError(response, "udid is required!");
            return;
        }
        data.put("udid", udid);

	    List<User> userList = talkService.getUserListByChannel(channelName);
	    Channel channel = talkService.getChannelByName(channelName);
	    
		data.put("userList", userList);
		data.put("channel", channel);
		
		processTemplate(response, "channel_user_list.html", data);
	}

	
}
