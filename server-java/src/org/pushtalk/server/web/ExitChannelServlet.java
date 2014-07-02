package org.pushtalk.server.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pushtalk.server.web.common.FreemarkerBaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExitChannelServlet extends FreemarkerBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = LoggerFactory.getLogger(ExitChannelServlet.class);


	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("action - exit channel");
	    
	    String udid = request.getParameter("udid");
	    if (null == udid) {
	        responseError(response, "udid is required!");
	        return;
	    }
	    
	    String channelName = request.getParameter("channel_name");
        if (null == channelName) {
            responseError(response, "channelName is required!");
            return;
        }
        
	    talkService.exitChannel(channelName, udid);
	    
	    response.sendRedirect("/main?udid=" + udid + "&channelDeleted=true");
	}

	
}
