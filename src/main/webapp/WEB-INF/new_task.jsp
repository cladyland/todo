<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/todo/new-task" method="post">
    <input name="title">
    <br>
    <input name="description">
    <br>
    <select multiple name="taskTags" size="4">
    <c:forEach items="${sessionScope.tags}" var="tag">
        <option value="${tag.getId()}">${tag.getTitle()}</option>
    </c:forEach>
        <c:forEach items="${sessionScope.userTags}" var="tag">
            <option value="${tag.getId()}">${tag.getTitle()}</option>
        </c:forEach>

    </select>
    <button type="submit">Add task</button>
</form>
</body>
</html>
