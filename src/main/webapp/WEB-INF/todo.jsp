<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/todo/new-task" method="get">
    <button type="submit">Add task</button>
</form>

<table>
    <tr>
        <td>Title</td>
        <td>Description</td>
        <td>Actions</td>
    </tr>

    <c:forEach items="${sessionScope.tasks}" var="task">
        <tr>
            <td> ${task.getTitle()} </td>
            <td> ${task.getDescription()} </td>
            <td>
                <form action="${pageContext.request.contextPath}/todo" method="post">
                <button type="submit" name="update" value="${task.getId()}">Edit</button>
                </form>
                <form action="${pageContext.request.contextPath}/todo" method="post">
                <button type="submit" name="delete" value="${task.getId()}">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>

</table>

</body>
</html>
