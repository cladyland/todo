<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/todo" method="post">
    <input name="title">
    <input name="description">
    <button type="submit">Add task</button>
</form>

<c:forEach items="${tasks}" var="task">
        ${task.getTitle()}
</c:forEach>
</body>
</html>
