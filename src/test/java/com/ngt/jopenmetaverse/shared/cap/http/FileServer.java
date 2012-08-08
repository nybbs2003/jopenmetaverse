package com.ngt.jopenmetaverse.shared.cap.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class FileServer
{
	private int port;
	private String basePath;
	private Server server;
	
	public FileServer()
	{
		this(9999, ".");
	}
	
	public FileServer(int port, String basePath)
	{
		this.port = port;
		this.basePath = basePath;
		server = null;
	}
	
	public URI createURI(String path) throws URISyntaxException
	{
		return new URI("http", null, "127.0.0.1", port, path, null, null);
	}
	
	public void start() throws Exception
	{
		server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector);
 
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
 
        resource_handler.setResourceBase(basePath);
 
        ServletContextHandler postServletcontext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        postServletcontext.setContextPath("/");
        postServletcontext.addServlet(new ServletHolder(new PostServlet()),"/*");
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, postServletcontext, new DefaultHandler() });
        server.setHandler(handlers);
 
        server.start();
//        server.join();
	}
	
	public void stop() throws Exception
	{
		if(server!= null)
		server.stop();
	}
	
	public class PostServlet extends HttpServlet
	{
	    private String greeting="Hello World";
	    public PostServlet(){}
	    public PostServlet(String greeting)
	    {
	        this.greeting=greeting;
	    }
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
			String testSD = "[ \n" + 
					"{ \n" + 
					"\"region_id\": \"67153d5b-3659-afb4-8510-adda2c034649\", \n" + 
					"\"scale\": \"one minute\", \n" + 
					"\"simulator statistics\": { \n" + 
					"\"time dilation\": 0.9878624, \n" + 
					"\"sim fps\": 44.38898, \n" + 
//					"\"agent updates per second\": NaN, \n" + 
					"\"total task count\": 4.0, \n" + 
					"\"active task count\": 0.0, \n" + 
					"\"pending uploads\": 0.0001096525 \n" + 
					"}\n" + 
					"}\n" + 
					"]";
	    	
	        response.setContentType("text/html");
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.getWriter().println(testSD);
	    }
	}	
}