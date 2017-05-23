package com.jx372.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jx372.mysite.dao.BoardDao;
import com.jx372.mysite.vo.BoardVo;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class ViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long no = WebUtils.checkParameter( request.getParameter( "no" ), 0L );
		int page = WebUtils.checkParameter( request.getParameter( "p" ), 1 );
		String keyword = WebUtils.checkParameter( request.getParameter( "kwd" ), "" );
		
		BoardDao dao = new BoardDao();
		BoardVo boardVo = dao.get( no );

		if( boardVo == null ) {
			WebUtils.redirect( request.getContextPath() + "/board", request, response );
			return;
		}
		
		// view count 증가
		dao.updateHit( no );
		
		request.setAttribute( "boardVo", boardVo );
		request.setAttribute( "page", page );
		request.setAttribute( "keyword", keyword );
		
		WebUtils.forward( "/WEB-INF/views/board/view.jsp", request, response );
	}
}
