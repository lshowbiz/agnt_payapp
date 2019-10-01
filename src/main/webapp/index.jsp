<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Expire" content="0">
<title>Index Page</title>
</head>
	<body>
		<li><a href="${pageContext.request.contextPath}/demo/400" target="main">demo web</a></li>
		<li><a href="${pageContext.request.contextPath}/demo/400.do" target="main">demo web.do</a></li>
		<li><a href="${pageContext.request.contextPath}/demo/xml/write/400.xml" target="main">demo xml</a></li>
		<li><a href="${pageContext.request.contextPath}/demo/json/write/400.json" target="main">demo json</a></li>
		<li><a href="${pageContext.request.contextPath}/demo/findall" target="main">demo findall</a></li>
		<li><a href="${pageContext.request.contextPath}/demo/showForm">demo add</a></li>
	</body>
</html>