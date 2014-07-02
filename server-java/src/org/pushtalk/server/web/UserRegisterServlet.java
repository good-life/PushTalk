package org.pushtalk.server.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pushtalk.server.model.User;
import org.pushtalk.server.utils.ServiceUtils;
import org.pushtalk.server.utils.StringUtils;
import org.pushtalk.server.web.common.FreemarkerBaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegisterServlet extends FreemarkerBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = LoggerFactory.getLogger(UserRegisterServlet.class);

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	    
	    LOG.debug("action - user register");
	    
	    String udid = request.getParameter("udid");
	    if (null == udid) {
	        responseError(response, "udid is required! " +
	        		"\n\nPush Talk server only support Android/iOS WebView client, " +
	        		"don't support accessing from web browser directly.");
	        return;
	    }
	    
	    String username = request.getParameter("username");
	    if (StringUtils.isTrimedEmpty(username)) {
	        responseError(response, "Username is required!");
	        return;
	    }
	    
        username = username.trim().toLowerCase();
        
        if (!ServiceUtils.isValidAliasOrTag(username)) {
            responseError(response, "Invalid username - " + username);
            return;
        }
        
        User user = talkService.getUserByName(username);
        if (null != user) {
            responseError(response, "Duplicated username - " + username);
            return;
        }
        
	    boolean ret = talkService.registerUser(udid, username);
	    if (!ret) {
	        LOG.warn("Failed to register user!");
	    }
	    
	    response.sendRedirect("/main?udid=" + udid + "&usernameUpdated=true");
	}

	
}
