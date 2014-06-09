package org.pushtalk.server.api.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pushtalk.server.utils.JPushService;
import org.pushtalk.server.web.common.NormalBaseServlet;

public class TestServlet extends NormalBaseServlet
{

    private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(TestServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOG.debug("api - /test/test");

        // List<User> users = User.getAllUsers();
        // RightJson rj = new RightJson(1001, users);

        JPushService.sendMsgTo("你好","18611343321");

        response.getOutputStream().write(gson.toJson("").getBytes());
    }
}
