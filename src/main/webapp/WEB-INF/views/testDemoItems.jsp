<%@ include file="../common/includes.jsp"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
<body>
	<table border="1" width="100%">
		<tr><td><s:message code="demo.id"/></td>
			<td><s:message code="demo.name"/></td>
			<td><s:message code="demo.age"/></td>
			<td><s:message code="demo.createdate"/></td>
		</tr>
		
		<c:forEach var="testDemo" items="${results }">
		<tr>
			<td>${testDemo.id }</td>
			<td>${testDemo.name }</td>
			<td>${testDemo.age }</td>
			<td>${testDemo.createDate }</td>
		</tr>
		</c:forEach>
	</table>
</body>



</html>