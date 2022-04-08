package com.company.model2board.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.company.model2board.common.JDBCUtil;

public class BoardDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	// 전체 게시글 목록 보기 메소드
	public List<BoardDO> getBoardList(String searchField, String searchText) {
		System.out.println("===> getBoardList() 메소드 처리됨!");
		
		List<BoardDO> boardList = new ArrayList<BoardDO>();
		
		try {
			conn = JDBCUtil.getConnection();
			String where = "";
			
			if (searchField != null && searchText != null) {
				where = "where "+searchField+" like '%"+searchText+"%'";
			}
			String Condition_SQL = "select * from board "+where+" order by seq desc";
			pstmt = conn.prepareStatement(Condition_SQL);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDO board = new BoardDO();
				board.setSeq(rs.getInt("SEQ"));
				board.setTitle(rs.getString("TITLE"));
				board.setWriter(rs.getString("WRITER"));
				board.setContent(rs.getString("CONTENT"));
				board.setRegdate(rs.getDate("REGDATE"));
				board.setCnt(rs.getInt("CNT"));
				
				boardList.add(board);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(rs, pstmt, conn);
		}
		return boardList;
	}  // END getBoardList() 메소드 =================================================
	
	// 게시글 상세보기 메소드
	public BoardDO getBoard(BoardDO boardDO) {
		System.out.println("===> getBoard() 메소드 처리됨!");
		
		BoardDO board = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			String UPDATE_CNT = "update board set cnt=cnt+1 where seq=?";
			pstmt = conn.prepareStatement(UPDATE_CNT);
			pstmt.setInt(1, boardDO.getSeq());
			pstmt.executeUpdate();
			
			String BOARD_GET = "select * from board Where seq=?";
			pstmt = conn.prepareStatement(BOARD_GET);
			pstmt.setInt(1, boardDO.getSeq());
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				board = new BoardDO();
				board.setSeq(rs.getInt("SEQ"));
				board.setTitle(rs.getString("TITLE"));
				board.setWriter(rs.getString("WRITER"));
				board.setContent(rs.getString("CONTENT"));
				board.setRegdate(rs.getDate("REGDATE"));
				board.setCnt(rs.getInt("CNT"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(rs, pstmt, conn);
		}
		return board;
	}  // END getBoard() 메소드 ===================================================
	
	// 게시글 등록 메소드
	public void insertBoard(BoardDO boardDO) {
		System.out.println("===> JDBC로 insertBoard() 처리됨!");
		
		BoardDO board = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			String BOARD_INSERT 
				= "insert into board(seq,title,writer,content) values((select nvl(max(seq),0)+1 from board),?,?,?)";
			pstmt = conn.prepareStatement(BOARD_INSERT);
			pstmt.setString(1, boardDO.getTitle());
			pstmt.setString(2, boardDO.getWriter());
			pstmt.setString(3, boardDO.getContent());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(pstmt, conn);
		}
	}  // END insertBoard() 메소드 ===============================================
	
	// 게시글 수정 메소드
	public void updateBoard(BoardDO boardDO) {
		System.out.println("===> JDBC로 updateBoard() 처리됨!");
		
		try {
			conn = JDBCUtil.getConnection();
			
			String BOARD_UPDATE = "update board set title=?, content=? where seq=?";
			pstmt = conn.prepareStatement(BOARD_UPDATE);
			pstmt.setString(1, boardDO.getTitle());
			pstmt.setString(2, boardDO.getContent());
			pstmt.setInt(3, boardDO.getSeq());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(pstmt, conn);
		}
	}  // END updateBoard() 메소드 ===============================================
	
	// 게시글 삭제 메소드
	public void deleteBoard(BoardDO boardDO) {
		System.out.println("===> JDBC로 deleteBoard() 처리됨!");
		
		try {
			conn = JDBCUtil.getConnection();
			
			String DELETE_BOARD = "delete from board where seq=?";
			pstmt = conn.prepareStatement(DELETE_BOARD);
			pstmt.setInt(1, boardDO.getSeq());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(pstmt, conn);			
		}
	}
}
