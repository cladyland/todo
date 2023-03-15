<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="../basis/head.jsp"/>
</head>
<body>
<div class="position-absolute top-50 start-50 translate-middle">
    <form action="${pageContext.request.contextPath}/todo/new-task" method="post">
        <label for="title">Title:</label><br>
        <input id="title" name="title">
        <br>
        <label for="description">Description:</label><br>
        <textarea id="description" name="description" placeholder="write more information"
                  style="width: 500px"></textarea>
        <br>
        <label for="task_tags">Tags:</label><br>
        <select id="task_tags" multiple name="taskTags" size="4">
            <c:forEach items="${sessionScope.tags}" var="tag">
                <option value="${tag.getId()}" style="background-color: ${tag.getColor()}">${tag.getTitle()}</option>
            </c:forEach>
            <c:forEach items="${sessionScope.userTags}" var="tag">
                <option value="${tag.getId()}" style="background-color: ${tag.getColor()}">${tag.getTitle()}</option>
            </c:forEach>
        </select><br>
        <label for="priority">Priority:</label>
        <select id="priority" name="priority">
            <c:forEach items="${priorities}" var="priority">
                <option value="${priority.getValue()}">${priority.getValue()}</option>
            </c:forEach>
        </select><br>
        <label for="status">Status:</label>
        <select id="status" name="status">
            <c:forEach items="${statuses}" var="status">
                <option value="${status.getValue()}">${status.getValue()}</option>
            </c:forEach>
        </select><br>
        <button type="submit" class="btn btn-primary">Add new task</button>
    </form>
</div>
</body>
</html>
