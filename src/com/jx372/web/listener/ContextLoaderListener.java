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
    	
    	System.out.println( "컨테이너 시작 하였습니다. - " + contextConfigLocation );
    }
	
    public void contextDestroyed(ServletContextEvent servletContextEvent)  { 
    }

	
}
