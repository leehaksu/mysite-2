package com.jx372.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jx372.mysite.vo.BoardVo;

public class BoardDao {

	private Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			//1. 드라이버 로딩
			Class.forName( "com.mysql.jdbc.Driver" );
			
			//2. Connection 하기
			String url = "jdbc:mysql://localhost:3306/webdb?useUnicode=true&characterEncoding=utf8";
			conn = DriverManager.getConnection( url, "webdb", "webdb" );
		} catch( ClassNotFoundException e ) {
			System.out.println( "JDBC Driver 를 찾을 수 없습니다." );
		} 
		
		return conn;
	}
	
	public List<BoardVo> getList( int page ) {
		List<BoardVo> list = new ArrayList<BoardVo>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = 
				"   select a.no, title, content, hit, date_format(reg_date, '%Y-%m-%d %p %h:%i:%s'), b.no, b.name" +
				"     from board a, user b" +
				"    where a.user_no = b.no" +
				" order by a.group_no desc, a.order_no asc";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			while( rs.next() ) {
				Long no = rs.getLong( 1 );
				String title = rs.getString( 2 );
				String content = rs.getString( 3 );
				Integer hit = rs.getInt( 4 );
				String regDate = rs.getString( 5 );
				Long userNo = rs.getLong( 6 );
				String userName = rs.getString( 7 );
				
				BoardVo vo = new BoardVo();
				vo.setNo( no );
				vo.setTitle(title);
				vo.setContent(content);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setUserNo(userNo);
				vo.setUserName(userName);
				
				list.add( vo );
			}
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null ) {
					rs.close();
				}
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			}catch( SQLException e ) {
				e.printStackTrace();
			} 
		}
		
		return list;
	}
	
	public boolean insert( BoardVo vo ) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			if( vo.getGroupNo() == 0 ) { // 새글
				String sql = 
					" insert" +
					"   into board" +
					" values (null, ?, ?, 0, now(), (select ifnull(max(group_no), 0) + 1 from board a), 1, 0, ?)";

				pstmt = conn.prepareStatement( sql );
				
				pstmt.setString( 1, vo.getTitle() );
				pstmt.setString( 2, vo.getContent() );
				pstmt.setLong( 3, vo.getUserNo() );
			} else { // 답글
				String sql = 
						" insert" +
						"   into board" +
						" values (null, ?, ?, 0, now(), ?, ?, ?, ?)";

				pstmt = conn.prepareStatement( sql );
				
				pstmt.setString( 1, vo.getTitle() );
				pstmt.setString( 2, vo.getContent() );
				pstmt.setInt( 3, vo.getGroupNo() );
				pstmt.setInt( 4, vo.getOrderNo() );
				pstmt.setInt( 5, vo.getDepth() );
				pstmt.setLong( 6, vo.getUserNo() );
			}
			
			int count = pstmt.executeUpdate();
			return count == 1;
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			}catch( SQLException e ) {
				e.printStackTrace();
			} 
		}
		
		return false;
	}
	
}
