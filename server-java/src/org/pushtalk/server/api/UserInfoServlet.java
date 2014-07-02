package org.pushtalk.server.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pushtalk.server.model.Channel;
import org.pushtalk.server.model.User;
import org.pushtalk.server.utils.ServiceUtils;
import org.pushtalk.server.web.common.NormalBaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class UserInfoServlet extends NormalBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = LoggerFactory.getLogger(UserInfoServlet.class);
    
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LOG.debug("api - user info");
        
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
        
        Set<String> channels = new HashSet<String>();
        for (Channel channel : user.getChannelList()) {
            LOG.debug("channel: " + channel.getName());
            channels.add(ServiceUtils.postfixAliasAndTag(channel.getName()));
        }
        
        
        Gson gson = new Gson();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", ServiceUtils.postfixAliasAndTag(user.getName()));
        params.put("channels", channels);
        
        String info = gson.toJson(params);
        LOG.info("The user info: " + info);
        
        response.getOutputStream().write(info.getBytes());
    }

	
}
