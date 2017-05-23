package com.jx372.mysite.action.guestbook;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jx372.mysite.dao.GuestbookDao;
import com.jx372.mysite.vo.GuestbookVo;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class AddAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String name = WebUtils.checkParameter( request.getParameter( "name" ), "" );
		String passwd = WebUtils.checkParameter( request.getParameter( "passwd" ), "" );
		String message = WebUtils.checkParameter( request.getParameter( "message" ), "" );

		GuestbookVo vo = new GuestbookVo();
		vo.setName(name);
		vo.setPasswd(passwd);
		vo.setMessage(message);
	
		new GuestbookDao().insert(vo);
		WebUtils.redirect( request.getContextPath() + "/guestbook", request, response);
	}

}
