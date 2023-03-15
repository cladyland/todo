<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="../basis/head.jsp"/>
    <style>
        form {
            display: inline-block;
            width: auto;
        }

        div {
            display: flex;
            justify-content: center;
            align-items: center;
            text-align: center;
            min-height: 100vh;
        }
    </style>
</head>
<body>
<form action="${pageContext.request.contextPath}/todo/new-task" method="get">
    <button type="submit" class="btn btn-success">Add task</button>
</form>
<form action="${pageContext.request.contextPath}/todo/new-tag" method="get">
    <button type="submit" class="btn btn-light">Add tag</button>
</form>
<div>
    <table class="table table-success table-striped" style="width: 90%">
        <tr style="text-align: center; font-family: fantasy">
            <td>Title</td>
            <td>Description</td>
            <td>Priority</td>
            <td>Status</td>
            <td>Tags</td>
            <td>Actions</td>
        </tr>
        <c:forEach items="${sessionScope.tasks}" var="task">
            <tr style="text-align: center; font-family: Georgia, serif">
                <td><i> ${task.getTitle()} </i></td>
                <td> ${task.getDescription()} </td>
                <td> ${task.getPriority()} </td>
                <td> ${task.getStatus()} </td>
                <td>
                    <c:forEach items="${task.getTags()}" var="tag">
                        <span style="background-color: ${tag.getColor()}">${tag.getTitle()}</span>
                    </c:forEach>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/todo/info" method="get">
                        <button type="submit" class="btn btn-info" name="moreInfo" value="${task.getId()}">More</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/todo" method="post">
                        <button type="submit" class="btn btn-warning" name="update" value="${task.getId()}">Edit
                        </button>
                    </form>
                    <form action="${pageContext.request.contextPath}/todo" method="post">
                        <button type="submit" class="btn btn-dark" name="delete" value="${task.getId()}">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
