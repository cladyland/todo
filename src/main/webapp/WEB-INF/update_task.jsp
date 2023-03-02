<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:forEach items="${task}" var="tas">

    <form action="${pageContext.request.contextPath}/todo" method="post">
        <input name="title" value="${tas.getTitle()}">
        <input name="description" value="${tas.getDescription()}">

        <button type="submit" name="saveUpdate" value="${tas.getId()}">Update</button>
    </form>

</c:forEach>
</body>
</html>
