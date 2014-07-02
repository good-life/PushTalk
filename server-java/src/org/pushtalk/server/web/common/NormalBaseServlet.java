package org.pushtalk.server.web.common;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pushtalk.server.data.TalkService;
import org.pushtalk.server.data.h2.TalkServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class NormalBaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1138134467003532522L;
    private static final Logger LOG = LoggerFactory.getLogger(NormalBaseServlet.class);

    protected static TalkService talkService;
    protected static Gson gson = new Gson();


    protected void responseError(HttpServletResponse response, String s) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("<h3>" + s + "</h3>");
    }

    protected void responseContent(HttpServletResponse response, String s) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(s);
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
    


    @Override
    public void init() throws ServletException {
        if (null == talkService) {
            try {
                talkService = new TalkServiceImpl();
            } catch (SQLException e) {
                LOG.error("Init service error", e);
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

}
