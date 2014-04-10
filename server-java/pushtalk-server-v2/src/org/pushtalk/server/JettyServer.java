package org.pushtalk.server;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.pushtalk.server.api.friend.FriendAddServlet;
import org.pushtalk.server.api.friend.FriendDeleteServlet;
import org.pushtalk.server.api.friend.FriendListServlet;
import org.pushtalk.server.api.friend.FriendSearchServlet;
import org.pushtalk.server.api.user.TestServlet;
import org.pushtalk.server.api.user.UserInfoServlet;
import org.pushtalk.server.api.user.UserLoginServlet;
import org.pushtalk.server.api.user.UserRegisterServlet;
import org.pushtalk.server.api.user.UserUpdateServlet;

public class JettyServer
{
    static Logger LOG = Logger.getLogger(JettyServer.class);

    public static void main(String[] args) throws Exception
    {
        int port = 10012;

        if (args.length >= 1)
        {
            String sPort = args[0];
            try
            {
                port = Integer.parseInt(sPort);
            } catch (NumberFormatException e)
            {
                LOG.info("Invalid port arg - " + sPort + ". User default value - " + port);
            }
        }

        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/v2");
        server.setHandler(context);

        // user
        context.addServlet(new ServletHolder(new UserLoginServlet()), "/user/login");
        context.addServlet(new ServletHolder(new UserUpdateServlet()), "/user/update");
        context.addServlet(new ServletHolder(new UserRegisterServlet()), "/user/register");
        context.addServlet(new ServletHolder(new UserInfoServlet()), "/user/info");

        // friend
        context.addServlet(new ServletHolder(new FriendAddServlet()), "/friend/add");
        context.addServlet(new ServletHolder(new FriendDeleteServlet()), "/friend/delete");
        context.addServlet(new ServletHolder(new FriendSearchServlet()), "/friend/search");
        context.addServlet(new ServletHolder(new FriendListServlet()), "/friend/list");

        // test api
        context.addServlet(new ServletHolder(new TestServlet()), "/test/test");

        server.start();
        LOG.info("Push Talk Server is started. Version:" + Config.VERSION);
        server.join();

    }
}
