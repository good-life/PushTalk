package org.pushtalk.server.api.friend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pushtalk.server.utils.JPushService;
import org.pushtalk.server.utils.RightJson;
import org.pushtalk.server.web.common.NormalBaseServlet;

public class FriendAddServlet extends NormalBaseServlet
{

    private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(FriendAddServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOG.debug("api - /friend/add");

        String friend_name = request.getParameter("friend_name");
        String user_name = request.getParameter("user_name");
        String request_msg = request.getParameter("request_msg");

        Map<String, String> msg_content = new HashMap<String, String>();
        msg_content.put("user_name", user_name);
        msg_content.put("request_msg", request_msg);

        Object msg = null;
        msg = new RightJson(3001, msg_content);
        JPushService.sendMsgTo(gson.toJson(msg), friend_name);

        response.getOutputStream().write(gson.toJson(msg).getBytes());
    }
}
