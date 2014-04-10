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

public class UserUpdateServlet extends NormalBaseServlet
{

    private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = Logger.getLogger(UserUpdateServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOG.debug("api - /user/update");
        Object result = null;
        User user = User.getUserBy(request.getParameter("username"));
        if (user == null)
        {
            LOG.error("No such user, or too much users be found!");
            result = new ErrorJson(1003, "No such user!");
        } else
        {
            LOG.info("");
            if (request.getParameter("userpwd") != null && request.getParameter("userpwd") != "")
            {
                user.setUserpwd(request.getParameter("userpwd"));
            }
            if (request.getParameter("mail") != null && request.getParameter("mail") != "")
            {
                user.setMail(request.getParameter("mail"));
            }
            if (request.getParameter("nickname") != null && request.getParameter("nickname") != "")
            {
                user.setNickname(request.getParameter("nickname"));
            }
            if (request.getParameter("birthday") != null && request.getParameter("birthday") != "")
            {
                user.setBirthday(request.getParameter("birthday"));
            }
            if (request.getParameter("signature") != null && request.getParameter("signature") != "")
            {
                user.setSignature(request.getParameter("signature"));
            }
            if (request.getParameter("gender") != null && request.getParameter("gender") != "")
            {
                user.setGender(request.getParameter("gender"));
            }
            if (request.getParameter("region") != null && request.getParameter("region") != "")
            {
                user.setRegion(request.getParameter("region"));
            }

            if (user.updateUserinfo() == 1)
            {
                result = new RightJson(3000, "Do post succeed!");
            } else
            {
                result = new ErrorJson(1004, "Update db failed!");
            }
        }

        response.getOutputStream().write(gson.toJson(result).getBytes());
    }
}
