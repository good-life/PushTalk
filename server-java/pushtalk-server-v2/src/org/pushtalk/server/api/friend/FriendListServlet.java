package org.pushtalk.server.api.friend;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pushtalk.server.model.Friendship;
import org.pushtalk.server.model.User;
import org.pushtalk.server.web.common.NormalBaseServlet;

public class FriendListServlet extends NormalBaseServlet {

	private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(FriendListServlet.class);
    

    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LOG.debug("api - /friend/list");

        Object result = null;
        User user= User.getUserBy(request.getParameter("username"));
        List<Friendship> fs = Friendship.getAllFriendshipBy(user.getId());

        response.getOutputStream().write(gson.toJson("api - /friend/list").getBytes());   
    }
}
