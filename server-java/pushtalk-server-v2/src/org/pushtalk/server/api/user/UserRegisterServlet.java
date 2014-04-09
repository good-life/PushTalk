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

public class UserRegisterServlet extends NormalBaseServlet
{

    private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(UserRegisterServlet.class);

    
    // Get username and userpwd to register as a new user.
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOG.debug("api - /user/register");
        
        Object resultJson = null;
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setUserpwd(request.getParameter("userpwd"));
        if (user.isExist())
        {
            LOG.debug("User exists, do not insert user.");
            resultJson = new ErrorJson(1002, "User exists!");
        } else
        {
            LOG.debug("User does not exist, insert user.");
            if (user.insertUser() == 1)
            {
                resultJson = new RightJson(3000, "Rigestration succeed!");
            } else
            {
                resultJson = new ErrorJson(1001, "DB insert error!");
            }
        }

        response.getOutputStream().write(gson.toJson(resultJson).getBytes());
    }
}
