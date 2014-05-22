package org.pushtalk.server.api.friend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pushtalk.server.model.Friendship;
import org.pushtalk.server.utils.JPushService;
import org.pushtalk.server.utils.RightJson;
import org.pushtalk.server.web.common.NormalBaseServlet;

public class FriendConfirmServlet extends NormalBaseServlet
{

    private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(FriendConfirmServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOG.debug("api - /friend/confirm");

        Object result = null;
        String friend_name = request.getParameter("friend_name");
        String user_name = request.getParameter("user_name");
        String confirm_msg = request.getParameter("confirm_msg");

        // insert the friendship into the database
        Friendship.addFriendship(user_name, friend_name);
        Friendship.addFriendship(friend_name, user_name);

        Map<String, String> msg_content = new HashMap<String, String>();
        msg_content.put("user_name", user_name);
        msg_content.put("confirm_msg", confirm_msg);

        Object msg = null;
        msg = new RightJson(3002, msg_content);
        JPushService.sendMsgTo(gson.toJson(msg), friend_name);

        result = new RightJson(3000, "Do post succeed!");

        response.getOutputStream().write(gson.toJson(result).getBytes());
    }
}
