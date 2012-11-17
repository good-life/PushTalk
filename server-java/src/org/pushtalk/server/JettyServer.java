package org.pushtalk.server;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.pushtalk.server.api.GetRecentChatsServlet;
import org.pushtalk.server.api.GetRecentMessagesServlet;
import org.pushtalk.server.api.ReceivedMessageServlet;
import org.pushtalk.server.api.UserInfoServlet;
import org.pushtalk.server.web.AllChannelListServlet;
import org.pushtalk.server.web.ChannelUserListServlet;
import org.pushtalk.server.web.ChattingServlet;
import org.pushtalk.server.web.EnterChannelServlet;
import org.pushtalk.server.web.ExitChannelServlet;
import org.pushtalk.server.web.MainServlet;
import org.pushtalk.server.web.NewChannelServlet;
import org.pushtalk.server.web.RootServlet;
import org.pushtalk.server.web.TalkServlet;
import org.pushtalk.server.web.UserChangeNameServlet;
import org.pushtalk.server.web.UserRegisterServlet;

public class JettyServer {
    static Logger LOG = Logger.getLogger(JettyServer.class);
    
	public static void main(String[] args) throws Exception {
		int port = 10010;
		
		if (args.length >= 1) {
			String sPort = args[0];
			try {
				port = Integer.parseInt(sPort);
			} catch (NumberFormatException e) {
				LOG.info("Invalid port arg - " + sPort + ". User default value - " + port);
			}
		}
		
		Server server = new Server(port);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        // channel
        context.addServlet(new ServletHolder(new AllChannelListServlet()), "/channel");
        context.addServlet(new ServletHolder(new NewChannelServlet()), "/channel/new");
        context.addServlet(new ServletHolder(new EnterChannelServlet()), "/channel/enter");
        context.addServlet(new ServletHolder(new ExitChannelServlet()), "/channel/exit");
        context.addServlet(new ServletHolder(new ChannelUserListServlet()), "/channel/users");
        
        // user
        context.addServlet(new ServletHolder(new RootServlet()), "/");
        context.addServlet(new ServletHolder(new MainServlet()), "/main");
        context.addServlet(new ServletHolder(new UserRegisterServlet()), "/user/register");
        context.addServlet(new ServletHolder(new UserChangeNameServlet()), "/user/changeName");
        
        // chatting
        context.addServlet(new ServletHolder(new TalkServlet()), "/talk");
        context.addServlet(new ServletHolder(new ChattingServlet()), "/chatting");
        
        // ajax & api
        context.addServlet(new ServletHolder(new UserInfoServlet()), "/api/user");
        context.addServlet(new ServletHolder(new ReceivedMessageServlet()), "/api/receivedMessage");
        context.addServlet(new ServletHolder(new GetRecentMessagesServlet()), "/api/getRecentMessages");
        context.addServlet(new ServletHolder(new GetRecentChatsServlet()), "/api/getRecentChats");
        
        server.start();
		server.join();
		LOG.info("Jetty Server started with " + port);
		
		
	}
}
