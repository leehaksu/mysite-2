package com.jx372.mysite.action.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jx372.mysite.dao.BoardDao;
import com.jx372.mysite.vo.BoardVo;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class ListAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int page = WebUtils.checkParameter( request.getParameter( "p" ), 1 );
		
		BoardDao dao = new BoardDao();
		List<BoardVo> list = dao.getList( page );
		
		request.setAttribute( "list", list );
		WebUtils.forward( "/WEB-INF/views/board/list.jsp", request, response);
	}

}
