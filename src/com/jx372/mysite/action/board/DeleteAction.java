package com.jx372.mysite.action.board;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jx372.mysite.dao.BoardDao;
import com.jx372.mysite.vo.UserVo;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class DeleteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// is auth?
		HttpSession session = request.getSession();
		if( session == null ) {
			WebUtils.redirect( request.getContextPath() + "/board", request, response );
			return;
		}
		UserVo authUser = (UserVo)session.getAttribute( "authUser" );
		if( authUser == null ) {
			WebUtils.redirect( "/mysite/board", request, response );
			return;
		}	
		
		long no = WebUtils.checkParameter( request.getParameter( "no" ), 0L );
		int page = WebUtils.checkParameter( request.getParameter( "p" ), 1 ); 
		String keyword = WebUtils.checkParameter( request.getParameter( "kwd" ), "" );
		long userNo = authUser.getNo();
		
		new BoardDao().delete( no, userNo );
		
		WebUtils.redirect( "/mysite/board?p=" + page + "&kwd=" + URLEncoder.encode( keyword, "UTF-8" ), request, response );
	}
}