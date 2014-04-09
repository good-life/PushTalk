package org.pushtalk.server.api.friend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pushtalk.server.web.common.NormalBaseServlet;

public class FriendAddServlet extends NormalBaseServlet {

	private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(FriendAddServlet.class);
    

    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LOG.debug("api - /friend/add");

        response.getOutputStream().write(gson.toJson("api - /friend/add").getBytes());   
    }
}
