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
	
	public List<BoardVo> getList( String keyword, Integer page, Integer size ) {
		
		List<BoardVo> list = new ArrayList<BoardVo>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			if( "".equals( keyword ) ) {
				String sql = 
					"   select a.no, a.title, a.hit, date_format(a.reg_date, '%Y-%m-%d %p %h:%i:%s'), a.depth, b.name, a.user_no" +
					"     from board a, user b" +
					"    where a.user_no = b.no" +
	                " order by group_no desc, order_no asc" +
	                "    limit ?, ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt( 1, (page-1)*size );
				pstmt.setInt( 2, size );
			} else {
				String sql =
					"   select a.no, a.title, a.hit, date_format(a.reg_date, '%Y-%m-%d %p %h:%i:%s'), a.depth, b.name, a.user_no" +
					"     from board a, user b" +
					"    where a.user_no = b.no" +
					"      and (title like ? or content like ?)" + 
					" order by group_no desc, order_no asc" +
				    "    limit ?, ?";
				
				pstmt = conn.prepareStatement(sql);

				pstmt.setString( 1, "%" + keyword + "%" );
				pstmt.setString( 2, "%" + keyword + "%" );
				pstmt.setInt( 3, (page-1)*size );
				pstmt.setInt( 4, size );
			}
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				long no = rs.getLong( 1 );
				String title = rs.getString( 2 );
				int hit = rs.getInt( 3 );
				String regDate = rs.getString( 4 );
				int depth = rs.getInt( 5 );
				String userName = rs.getString( 6 );
				long userNo = rs.getLong( 7 );
				
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				vo.setUserName(userName);
				vo.setUserNo(userNo);
				
				list.add( vo );
			}
			
		} catch (SQLException e) {
			System.out.println( "error:" + e );
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
			} catch ( SQLException e ) {
				System.out.println( "error:" + e );
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
	
	public void delete( Long boardNo, Long userNo ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "delete from board where no = ? and user_no = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong( 1, boardNo );
			pstmt.setLong( 2, userNo );
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println( "error:" + e );
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch ( SQLException e ) {
				System.out.println( "error:" + e );
			}  
		}
	}
	
	public int getTotalCount( String keyword ) {
		int totalCount = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			if( "".equals( keyword ) ) {
				String sql = "select count(*) from board";
				pstmt = conn.prepareStatement(sql);
			} else { 
				String sql =
					"select count(*)" +
					"  from board" +
					" where title like ? or content like ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, "%" + keyword + "%");
				pstmt.setString(2, "%" + keyword + "%");
			}
			rs = pstmt.executeQuery();
			if( rs.next() ) {
				totalCount = rs.getInt( 1 );
			}
		} catch (SQLException e) {
			System.out.println( "error:" + e );
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
			} catch ( SQLException e ) {
				System.out.println( "error:" + e );
			}  
		}
		
		return totalCount;
	}
	
	public void increaseGroupOrder( Integer groupNo, Integer orderNo ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "update board set order_no = order_no + 1 where group_no = ? and order_no > ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, groupNo );
			pstmt.setInt(2, orderNo );
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println( "error:" + e );
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch ( SQLException e ) {
				System.out.println( "error:" + e );
			}  
		}
	}
	
	public void updateHit( Long boardNo ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "update board set hit = hit + 1 where no = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, boardNo);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println( "error:" + e );
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch ( SQLException e ) {
				System.out.println( "error:" + e );
			}  
		}
	}	
	
	public void update( BoardVo vo ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "update board set title=?, content=? where no=? and user_no=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString( 1, vo.getTitle() );
			pstmt.setString( 2, vo.getContent() );
			pstmt.setLong( 3, vo.getNo() );
			pstmt.setLong( 4, vo.getUserNo() );
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println( "error:" + e );
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch ( SQLException e ) {
				System.out.println( "error:" + e );
			}  
		}
	}
	
	public BoardVo get( Long boardNo ) {
		BoardVo vo = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = 
				" select no, title, content, group_no, order_no, depth, user_no" +
				"   from board" +
				"  where no = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong( 1, boardNo );
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				long no = rs.getLong( 1 );
				String title = rs.getString( 2 );
				String content = rs.getString( 3 );
				int groupNo = rs.getInt( 4 );
				int orderNo = rs.getInt( 5 );
				int depth = rs.getInt( 6 );
				long userNo = rs.getLong( 7 );
				
				vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContent(content);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setUserNo(userNo);
			}
		} catch (SQLException e) {
			System.out.println( "error:" + e );
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
			} catch ( SQLException e ) {
				System.out.println( "error:" + e );
			}  
		}
		
		return vo;
	}	
	
}
