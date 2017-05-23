package com.jx372.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jx372.mysite.vo.UserVo;

public class UserDao {

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
	
	public UserVo get( Long no ) {
		UserVo vo = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = 
				" select no, name, email, gender" + 
				"   from user" + 
				"  where no=?";
			pstmt = conn.prepareStatement( sql );
			
			pstmt.setLong( 1, no );
			
			rs = pstmt.executeQuery();
			if( rs.next() ) {
				vo = new UserVo();
				vo.setNo( rs.getLong( 1 ) );
				vo.setName( rs.getString( 2 ) );
				vo.setEmail( rs.getString( 3 ) );
				vo.setGender( rs.getString( 4 ) );
			}
			
 		}catch( SQLException e ) {
			e.printStackTrace();
		}finally{
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return vo;
	}
	
	public UserVo get( String email, String password ) {
		UserVo vo = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = 
				" select no, name" + 
				"   from user" + 
				"  where email=?" +
				"    and password = password(?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString( 1, email );
			pstmt.setString( 2, password );
			
			rs = pstmt.executeQuery();
			if( rs.next() ) {
				Long no = rs.getLong( 1 );
				String name = rs.getString( 2 );
				
				vo = new UserVo();
				vo.setNo(no);
				vo.setName(name);
			}
			
 		}catch( SQLException e ) {
			e.printStackTrace();
		}finally{
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return vo;
	}
	
	public boolean update( UserVo vo ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();

			if( "".equals( vo.getPassword() ) ) {
				String sql = 
						" update user" +
						"    set name=?," +
						"        gender=?" +
						"  where no = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString( 1, vo.getName() );
					pstmt.setString( 2, vo.getGender() );
					pstmt.setLong( 3, vo.getNo() );
			} else {
				String sql = 
					" update user" +
					"    set name=?," +
					"        password=password(?)," +
					"        gender=?" +
					"  where no = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString( 1, vo.getName() );
				pstmt.setString( 2, vo.getPassword() );
				pstmt.setString( 3, vo.getGender() );
				pstmt.setLong( 4, vo.getNo() );				
			}
			
			int count = pstmt.executeUpdate();
			return count == 1;
			
		}catch( SQLException e ) {
			e.printStackTrace();
		}finally{
			try {
				if(conn != null ) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public boolean insert( UserVo vo ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = 
				" insert" +
				"   into user" +
				" values (null, ?, ?, password(?), ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString( 1, vo.getName() );
			pstmt.setString( 2, vo.getEmail() );
			pstmt.setString( 3, vo.getPassword() );
			pstmt.setString( 4, vo.getGender() );
			
			int count = pstmt.executeUpdate();
			return count == 1;
			
		}catch( SQLException e ) {
			e.printStackTrace();
		}finally{
			try {
				if(conn != null ) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
}
