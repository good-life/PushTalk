package org.pushtalk.server.api.friend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pushtalk.server.model.Friendship;
import org.pushtalk.server.web.common.NormalBaseServlet;

public class FriendGetaliasServlet extends NormalBaseServlet
{

    private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(FriendGetaliasServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOG.debug("api - /friend/getalias");

        String friend_name = request.getParameter("friend_name");
        String user_name = request.getParameter("user_name");
        
        String alias = Friendship.getAlias(user_name, friend_name);
        
        response.getOutputStream().write(gson.toJson(alias).getBytes());
    }
}
