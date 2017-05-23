package com.jx372.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jx372.mysite.dao.BoardDao;
import com.jx372.mysite.vo.BoardVo;
import com.jx372.mysite.vo.UserVo;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class ModifyFormAction implements Action {

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
			WebUtils.redirect( request.getContextPath() + "/board", request, response );
			return;
		}
		
		long no = WebUtils.checkParameter(request.getParameter("no"), 0L);
		String keyword = WebUtils.checkParameter( request.getParameter( "kwd" ), "" );

		BoardDao dao = new BoardDao();
		BoardVo boardVo = dao.get(no);

		request.setAttribute( "boardVo", boardVo );
		request.setAttribute( "keyword", keyword );
		
		WebUtils.forward( "/WEB-INF/views/board/modify.jsp", request, response );
	}
}
