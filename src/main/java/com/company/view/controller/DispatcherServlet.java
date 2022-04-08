package com.company.view.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.company.model2board.board.BoardDAO;
import com.company.model2board.board.BoardDO;
import com.company.model2board.user.UserDAO;
import com.company.model2board.user.UserDO;

public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DispatcherServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		process(request, response);

	}
	// 개발자 정의 메소드
	private void process(HttpServletRequest request, 
						 HttpServletResponse response) 
						 throws IOException {

		// 1. 클라이언트의 요청 path 정보를 추출한다.
		/* 아래 두 줄이 Model2 아키텍처 구조에서 가장 중요한 부분  */
		// http://localhost:8080/220405_Model2_Board/login.do
		String uri = request.getRequestURI();  // "/220405_Model2_Board/login.do"을 얻어옴.
		String path = uri.substring(uri.lastIndexOf("/"));  // "/login.do"을 얻어옴.
		
		if (path.equals("/login.do")) {
			System.out.println("로그인 처리됨!");
			
			// 1. 사용자 입력 정보 추출 (1,2,3을 컨트롤러 로직 코드라 한다.)
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			// 2. Model을 이용한 DB 연동 처리
			UserDO userDO = new UserDO();
			userDO.setId(id);
			userDO.setPassword(password);
			
			UserDAO userDAO = new UserDAO();
			UserDO user = userDAO.getUser(userDO);
			
			// 3. 화면 네비게이션(포워딩)
			if (user != null) {
				// System.out.println("로그인 성공");
				HttpSession session = request.getSession();
				session.setAttribute("IdKey", id);
				response.sendRedirect("getBoardList.do");
			} else {
				// System.out.println("로그인 실패");
				response.sendRedirect("login.jsp");
			}
		} else if (path.equals("/getBoardList.do")) {
			System.out.println("전체 게시글 목록 보기 처리됨!");
			
			String searchField = "";
			String searchText = "";
			
			// 사용자가 조건에 맞는 레코드만을 검색하는 경우
			if (request.getParameter("searchCondition") != "" &&
					request.getParameter("searchKeyword") != "") {
				// 1. 사용자 입력 정보 추출
				searchField = request.getParameter("searchCondition");
				searchText = request.getParameter("searchKeyword");
			}
			// 2. Model을 이용한 DB 연동 처리
			BoardDAO boardDAO = new BoardDAO();
			List<BoardDO> boardList = boardDAO.getBoardList(searchField, searchText);
			
			// 3. [중요] board 테이블의 select 결과를 세션에 저장한다.
			HttpSession session = request.getSession();
			session.setAttribute("boardList", boardList);
			
			// 4. 포워딩 => 응답
			response.sendRedirect("getBoardList.jsp");
		} else if (path.equals("/getBoard.do")) {
			System.out.println("게시글 상세보기 처리됨!");
			
			// 1. 검색 할 게시글 번호 추출
			String seq = request.getParameter("seq");
			
			// 2. Model을 이용한 DB 연동 처리
			BoardDO boardDO = new BoardDO();
			boardDO.setSeq(Integer.parseInt(seq));
			
			BoardDAO boardDAO = new BoardDAO();
			BoardDO board = boardDAO.getBoard(boardDO);
			
			// 3. [중요] board 테이블의 select 결과를 세션에 저장한다.
			HttpSession session = request.getSession();
			session.setAttribute("board", board);
			
			// 4. 포워딩 => 응답
			response.sendRedirect("getBoard.jsp");
		} else if (path.equals("/insertBoard.do")) {
			System.out.println("게시글 입력 처리됨!!");
			
			String title = request.getParameter("title");
			String writer = request.getParameter("writer");
			String content = request.getParameter("content");
			
			BoardDO boardDO = new BoardDO();
			boardDO.setTitle(title);
			boardDO.setWriter(writer);
			boardDO.setContent(content);
			
			BoardDAO boardDAO = new BoardDAO();
			boardDAO.insertBoard(boardDO);
			
			response.sendRedirect("getBoardList.do");
		} else if(path.equals("/updateBoard.do")) {
			System.out.println("게시글 수정 처리됨!");
			
			request.setCharacterEncoding("UTF-8");
			
			String seq = request.getParameter("seq");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			BoardDO boardDO = new BoardDO();
			boardDO.setSeq(Integer.parseInt(seq));
			boardDO.setTitle(title);
			boardDO.setContent(content);
			
			BoardDAO boardDAO = new BoardDAO();
			boardDAO.updateBoard(boardDO);
			
			response.sendRedirect("getBoardList.do");
		} else if(path.equals("/deleteBoard.do")) {
			System.out.println("게시글 삭제 처리됨!");
			
			String seq = request.getParameter("seq");
			
			BoardDO boardDO = new BoardDO();
			boardDO.setSeq(Integer.parseInt(seq));
			
			BoardDAO boardDAO = new BoardDAO();
			boardDAO.deleteBoard(boardDO);
			
			response.sendRedirect("getBoardList.do");
		} else if(path.equals("/logout.do")) {
			System.out.println("로그아웃 처리됨!");
			
			HttpSession session = request.getSession();
			session.invalidate();  // 세션 종료
			
			response.sendRedirect("login.jsp");
		}
	}
}
