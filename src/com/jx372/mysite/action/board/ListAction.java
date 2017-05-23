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
	private static final int LIST_SIZE = 5; //리스팅되는 게시물의 수
	private static final int PAGE_SIZE = 5; //페이지 리스트의 페이지 수
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1-1. 파라미터 page 가져오기
		int currentPage = WebUtils.checkParameter( request.getParameter( "p" ), 1 );
		//1-2. 파라미터 kwd 가져오기
		String keyword = WebUtils.checkParameter( request.getParameter( "kwd" ), "" );

		//2. dao 생성
		BoardDao dao = new BoardDao();

		//3. 페이징을 위한 기본 데이터 계산
		int totalCount = dao.getTotalCount( keyword ); 
		int pageCount = (int)Math.ceil( (double)totalCount / LIST_SIZE );
		int blockCount = (int)Math.ceil( (double)pageCount / PAGE_SIZE );
		int currentBlock = (int)Math.ceil( (double)currentPage / PAGE_SIZE );
		
		//4. 파라미터 page 값  검증
		if( currentPage < 1 ) {
			currentPage = 1;
			currentBlock = 1;
		} else if( currentPage > pageCount ) {
			currentPage = pageCount;
			currentBlock = (int)Math.ceil( (double)currentPage / PAGE_SIZE );
		}
		
		//5. view에서 페이지 리스트를 렌더링 하기위한 데이터 값 계산
		int beginPage = currentBlock == 0 ? 1 : (currentBlock - 1)*PAGE_SIZE + 1;
		int prevPage = ( currentBlock > 1 ) ? ( currentBlock - 1 ) * PAGE_SIZE : 0;
		int nextPage = ( currentBlock < blockCount ) ? currentBlock * PAGE_SIZE + 1 : 0;
		int endPage = ( nextPage > 0 ) ? ( beginPage - 1 ) + LIST_SIZE : pageCount;
		
		//6. 리스트 가져오기
		List<BoardVo> list = dao.getList( keyword, currentPage, LIST_SIZE );

		//7. request 범위에 저장
		request.setAttribute( "list", list );
		
		request.setAttribute( "totalCount", totalCount );
		request.setAttribute( "listSize", LIST_SIZE );
		request.setAttribute( "currentPage", currentPage );
		request.setAttribute( "beginPage", beginPage );
		request.setAttribute( "endPage", endPage );
		request.setAttribute( "prevPage", prevPage );
		request.setAttribute( "nextPage", nextPage );
		request.setAttribute( "keyword", keyword );
		
		//8. 포워딩
		WebUtils.forward( "/WEB-INF/views/board/list.jsp", request, response );
	}
}
