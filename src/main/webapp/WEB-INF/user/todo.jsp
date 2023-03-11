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
<form action="${pageContext.request.contextPath}/todo/new-tag" method="get">
    <button type="submit">Add tag</button>
</form>

<table>
    <tr>
        <td>Title</td>
        <td>Description</td>
        <td>Priority</td>
        <td>Status</td>
        <td>Tags</td>
        <td>Actions</td>
    </tr>

    <c:forEach items="${sessionScope.tasks}" var="task">
        <tr>
            <td> ${task.getTitle()} </td>
            <td> ${task.getDescription()} </td>
            <td> ${task.getPriority()} </td>
            <td> ${task.getStatus()} </td>
            <td>
                <c:forEach items="${task.getTags()}" var="tag">
                    ${tag.getTitle()}
                </c:forEach>
            </td>
            <td>
                <form action="${pageContext.request.contextPath}/todo/info" method="get">
                    <button type="submit" name="more" value="${task.getId()}">More</button>
                </form>
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
