package org.pushtalk.server.api.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pushtalk.server.model.User;
import org.pushtalk.server.utils.ErrorJson;
import org.pushtalk.server.utils.RightJson;
import org.pushtalk.server.web.common.NormalBaseServlet;

public class UserInfoServlet extends NormalBaseServlet
{

    private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(UserInfoServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOG.debug("api - /user/info");

        Object result = null;
        User user = User.getUserBy(request.getParameter("username"));
        if (user != null)
        {
            result = new RightJson(3006, user);
        } else
        {
            result = new ErrorJson(1003, "No such user!");
        }
        response.getOutputStream().write(gson.toJson(result).getBytes());
    }
}
