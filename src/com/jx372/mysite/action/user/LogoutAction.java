package com.jx372.mysite.action.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class LogoutAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		
		// 로그아웃 처리
		if( session != null && session.getAttribute( "authUser" ) != null ){
			session.removeAttribute( "authUser" );
			session.invalidate();
		}
		
		WebUtils.redirect( request.getContextPath() + "/main", request, response);
	}

}
