package com.jx372.mysite.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jx372.mysite.action.guestbook.GuestbookActionFactory;
import com.jx372.web.action.Action;
import com.jx372.web.action.ActionFactory;

@WebServlet("/guestbook")
public class GuestbookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request.setCharacterEncoding( "UTF-8" );
		String actionName = request.getParameter( "a" );
		
		ActionFactory af = new GuestbookActionFactory();
		Action action = af.getAction(actionName);
		action.execute(request, response);
	}
}