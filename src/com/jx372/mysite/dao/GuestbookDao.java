package com.jx372.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jx372.mysite.vo.GuestbookVo;

public class GuestbookDao {

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
	
	public List<GuestbookVo> getList() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();
		
		try {
			conn = getConnection();
			stmt = conn.createStatement();

			String sql = 
				"   select no, name, message, date_format(reg_date, '%Y-%m-%d')" +
				"     from guestbook" +
				" order by reg_date desc";
			rs = stmt.executeQuery( sql );
			
			while( rs.next() ) {
				Long no = rs.getLong( 1 );
				String name = rs.getString( 2 );
				String message = rs.getString( 3 );
				String regDate = rs.getString( 4 );
				
				GuestbookVo vo = new GuestbookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setMessage(message);
				vo.setRegDate(regDate);
				
				list.add( vo );
			}
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null ) {
					rs.close();
				}
				if( stmt != null ) {
					stmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean delete( GuestbookVo vo ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "delete from guestbook where no = ? and passwd = ?";
			pstmt = conn.prepareStatement( sql );
			
			pstmt.setLong( 1, vo.getNo() );
			pstmt.setString( 2, vo.getPasswd() );
			
			int count = pstmt.executeUpdate();
			
			return count == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean insert( GuestbookVo vo ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "insert into guestbook values( null, ?, ?, ?, now() )";
			pstmt = conn.prepareStatement( sql );
			
			pstmt.setString( 1, vo.getName() );
			pstmt.setString( 2, vo.getPasswd() );
			pstmt.setString( 3, vo.getMessage() );
			
			int count = pstmt.executeUpdate();
			
			return count == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
	}
}
