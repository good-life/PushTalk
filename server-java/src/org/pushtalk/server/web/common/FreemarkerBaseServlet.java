package org.pushtalk.server.web.common;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pushtalk.server.web.template.TemplateIndicator;

import com.google.gson.Gson;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public abstract class FreemarkerBaseServlet extends NormalBaseServlet {
	private static final long serialVersionUID = 4074412299657955880L;

	private Configuration configuration;
	private Gson gson = new Gson();
	
	public abstract void process(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		process(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		process(request, response);
	}
	
	public void processJSON(HttpServletResponse response, Map<String, Object> data) throws IOException {
		if (null == data) data = new HashMap<String, Object>();
		
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");
		
		Writer out = response.getWriter();  
		out.write(gson.toJson(data));
	}
	
	public void processTemplate(HttpServletResponse response, String templateFile, 
			Map<String, Object> data) throws IOException {
		if (null == data) data = new HashMap<String, Object>();
		Template template = configuration.getTemplate(templateFile);
		
		response.setContentType("text/html; charset=" + template.getEncoding());
		response.setCharacterEncoding("utf-8");
		
		Writer out = response.getWriter();  
		try {
			template.process(data, out);
		} catch (TemplateException e) {
			throw new IOException("Template error", e);
		}  
	}

	public void processTemplate(HttpServletResponse response, String templateFile) throws IOException {
		processTemplate(response, templateFile, null);
	}

	public void processMsg(HttpServletResponse response, String info) throws IOException {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("info", info);
		processTemplate(response, "info.html", data);
	}


	
	public String buildTemplate(HttpServletResponse response, String templateFile, 
			Map<String, Object> data) throws IOException {
		if (null == data) data = new HashMap<String, Object>();
		Template template = configuration.getTemplate(templateFile);
		
		StringWriter out = new StringWriter();
		try {
			template.process(data, out);
		} catch (TemplateException e) {
			throw new IOException("Template error", e);
		}
		
		return out.toString();
	}

	@Override
    protected void responseError(HttpServletResponse response, String s) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("<h3>"+ s +"</h3>");
	}
	
	@Override
	public void init() throws ServletException {
	    super.init();
	    
        configuration = new Configuration();
        configuration.setEncoding(Locale.CHINA, "UTF-8");
        
        configuration.setClassForTemplateLoading(TemplateIndicator.class, "");
    }
	
	
	@Override
	public void destroy() {
		super.destroy();
		if (configuration != null) {
			configuration = null;
		}
	}
	

}
