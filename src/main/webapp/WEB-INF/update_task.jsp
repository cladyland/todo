<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Title</title>
</head>
<body>

<form action="${pageContext.request.contextPath}/todo" method="post">
    <input name="title" value="<c:out value="${task.getTitle()}"></c:out>">
    <input name="description" value="<c:out value="${task.getDescription()}"></c:out>">

    <button type="submit" name="saveUpdate" value="${task.getId()}">Update</button>
</form>

</body>
</html>
