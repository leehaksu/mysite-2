package com.jx372.mysite.action.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jx372.mysite.dao.UserDao;
import com.jx372.mysite.vo.UserVo;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class LoginAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String email = request.getParameter( "email" );
		String password = request.getParameter( "password" );
		
		UserVo vo = new UserDao().get( email, password );
		if( vo == null ) { // 인증 실패
			//WebUtils.redirect( 
			//	request.getContextPath() + "/user?a=loginform&result=fail", request, response);
			request.setAttribute( "result", "fail" );
			WebUtils.forward(
				"/WEB-INF/views/user/loginform.jsp", 
				request, 
				response);
			return;
		}
		
		// 인증 처리
		HttpSession session = request.getSession( true );
		session.setAttribute( "authUser", vo );
		
		//main redirect
		WebUtils.redirect( request.getContextPath() + "/main", request, response);
	}

}
