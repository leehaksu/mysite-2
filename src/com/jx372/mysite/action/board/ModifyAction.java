package com.jx372.mysite.action.board;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jx372.mysite.dao.BoardDao;
import com.jx372.mysite.vo.BoardVo;
import com.jx372.mysite.vo.UserVo;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class ModifyAction implements Action {

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
		String title = WebUtils.checkParameter(request.getParameter("title"), "");
		String content = WebUtils.checkParameter(request.getParameter("content"), "");
		String keyword = WebUtils.checkParameter( request.getParameter( "kwd" ), "" );

		BoardVo vo = new BoardVo();
		vo.setNo(no);
		vo.setTitle(title);
		vo.setContent(content);
		vo.setUserNo( authUser.getNo() );

		new BoardDao().update(vo);

		WebUtils.redirect( request.getContextPath() + "/board?a=view&no=" + no + "&kwd=" + URLEncoder.encode( keyword, "UTF-8" ), request, response );
	}
}
