package org.pushtalk.server.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pushtalk.server.web.common.FreemarkerBaseServlet;

public class RootServlet extends FreemarkerBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;


	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	    
	    response.sendRedirect("/main");
	    
	}

	
}

