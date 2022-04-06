package com.company.view.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			
			// 1. 사용자 입력 정보 추출
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
				System.out.println("로그인 성공");
			} else {
				System.out.println("로그인 실패");
			}
		}
	}
}
