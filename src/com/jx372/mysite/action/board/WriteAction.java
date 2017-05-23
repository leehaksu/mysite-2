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

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//인증 체크
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
		
		//
		Long userNo = authUser.getNo();
		String title = request.getParameter( "title" );
		String content = request.getParameter( "content" );
		int groupNo = WebUtils.checkParameter( request.getParameter( "group_no" ), 0);
		int orderNo = WebUtils.checkParameter( request.getParameter( "order_no" ), 0);
		int depth = WebUtils.checkParameter( request.getParameter( "depth" ), 0);
		
		BoardVo vo = new BoardVo();
		vo.setTitle(title);
		vo.setContent(content);
		vo.setGroupNo( groupNo );
		vo.setOrderNo( orderNo );
		vo.setDepth( depth );
		vo.setUserNo(userNo);
		
		BoardDao dao = new BoardDao(); 
		dao.insert( vo );
		if( groupNo > 0 ) { // 답글 
			//dao.increaseOrderNo( groupNo, orderNo );
		}
		
		WebUtils.redirect( request.getContextPath() + "/board", request, response);
	}

}
