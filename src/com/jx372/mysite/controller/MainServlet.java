package com.jx372.mysite.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jx372.mysite.action.main.MainActionFactory;
import com.jx372.web.action.Action;

//@WebServlet("/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		String configPath = getServletConfig().getInitParameter( "config" );
		System.out.println( "Main Servlet Initialized -" + configPath );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request.setCharacterEncoding( "UTF-8" );
		String actionName = request.getParameter( "a" );
		Action action = 
				new MainActionFactory().getAction( actionName );
		action.execute(request, response);
	}
}
