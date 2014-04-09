package org.pushtalk.server.api.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pushtalk.server.model.User;
import org.pushtalk.server.web.common.NormalBaseServlet;

public class UserLoginServlet extends NormalBaseServlet
{

    private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(UserLoginServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOG.debug("api - /user/login");

        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setUserpwd(request.getParameter("userpwd"));
        
        LOG.debug(user.isExist());

        response.getOutputStream().write(gson.toJson("api - /user/login").getBytes());
    }
}
