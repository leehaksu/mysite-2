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

public class ModifyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//1. 인증여부
		HttpSession session = request.getSession();
		
		if( session == null ) {
			WebUtils.redirect( request.getContextPath() + "/user?a=loginform", request, response);
			return;
		}
		UserVo authUser = (UserVo)session.getAttribute( "authUser" );
		if( authUser == null ) {
			WebUtils.redirect( request.getContextPath() + "/user?a=loginform", request, response);
			return;
		}
		
		Long no = authUser.getNo();
		String name = request.getParameter( "name" );
		String password = request.getParameter( "password" );
		String gender = request.getParameter( "gender" );
		
		UserVo vo = new UserVo();
		vo.setNo(no);
		vo.setName(name);
		vo.setPassword(password);
		vo.setGender(gender);
		
		new UserDao().update( vo );
		
		//세션 내용 변경
		authUser.setName( name );
		
		WebUtils.redirect( request.getContextPath() + "/user?a=modifyform&result=success", request, response );
	}
}
