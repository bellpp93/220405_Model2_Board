<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>새 게시글 등록 페이지 => insertBoard.jsp</title>
<style>
	#div_box {
		position:absolute;
		top:10%;
		left:20%;
	}
</style>
</head>
<body>
	<div id="div_box">
		<h1>새 게시글 등록</h1>
		<a href="logout.do">로그아웃</a>
		<hr>
		<form name="insertForm" method="POST" action="insertBoard.do">
			<table border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td bgcolor="orange" width="70">제목</td>
					<td align="left"></td>
						<input type="text" name="title"/>
				</tr>
				<tr>
					<td bgcolor="orange">작성자</td>
					<td align="left"></td>
						<input type="text" name="writer"/>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>