package com.jx372.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//@WebListener
public class ContextLoaderListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent)  { 
    	String contextConfigLocation = 
    	servletContextEvent.
    	getServletContext().
    	getInitParameter( "contextConfigLocation" );
    	
    	System.out.println( "Container starts... " + contextConfigLocation );
    }
	
    public void contextDestroyed(ServletContextEvent servletContextEvent)  { 
    }

	
}
