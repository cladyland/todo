<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<jsp:include page="../basis/head.jsp"/>
<jsp:include page="../basis/logout.jsp"/>
<body>
<div class="text-center">
    <p class="hello-title">Hello, ${sessionScope.get("username")}!</p>
    <form class="main-button-form" action="${pageContext.request.contextPath}/todo/new-task" method="get">
        <button type="submit" class="btn btn-success">Add task</button>
    </form>
    <form class="main-button-form" action="${pageContext.request.contextPath}/todo/tags" method="get">
        <button type="submit" class="btn btn-light" name="fromTodo">Add tag</button>
    </form>
</div>
<div class="task-table">
    <table class="table table-success table-striped">
        <tr class="task-table-top">
            <td style="width: 10%">Title</td>
            <td style="width: 40%">Description</td>
            <td style="width: 5%">Priority</td>
            <td style="width: 5%">Status</td>
            <td style="width: 10%">Tags</td>
            <td style="width: 15%">Actions</td>
        </tr>
        <c:forEach items="${sessionScope.tasks}" var="task">
            <tr class="task-table-cell">
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
                    <form class="main-button-form" action="${pageContext.request.contextPath}/todo/info" method="get">
                        <button type="submit" class="btn btn-info" name="moreInfo" value="${task.getId()}">More</button>
                    </form>
                    <form class="main-button-form" action="${pageContext.request.contextPath}/todo" method="post">
                        <button type="submit" class="btn btn-warning" name="update"
                                value="${task.getId()}">Edit</button>
                    </form>
                    <form class="main-button-form" action="${pageContext.request.contextPath}/todo" method="post">
                        <button type="submit" class="btn btn-dark" name="delete" value="${task.getId()}"
                                onclick="return confirm('Delete task ${task.getTitle()}?')">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
