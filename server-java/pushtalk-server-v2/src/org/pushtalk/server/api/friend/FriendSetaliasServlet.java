package org.pushtalk.server.api.friend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pushtalk.server.model.Friendship;
import org.pushtalk.server.web.common.NormalBaseServlet;

public class FriendSetaliasServlet extends NormalBaseServlet
{

    private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(FriendSetaliasServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOG.debug("api - /friend/setalias");

        String friend_name = request.getParameter("friend_name");
        String user_name = request.getParameter("user_name");
        String alias = request.getParameter("alias");

        Friendship.setAlias(user_name, friend_name, alias);

        response.getOutputStream().write(gson.toJson("").getBytes());
    }
}
