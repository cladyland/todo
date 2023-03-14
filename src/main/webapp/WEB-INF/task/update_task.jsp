<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body onload="setSelectedAttributes()">
<form action="${pageContext.request.contextPath}/todo" method="post">
    <label for="title">Title:</label><br>
    <input id="title" name="title" value="<c:out value="${task.getTitle()}"></c:out>"><br>
    <label for="description">Description:</label><br>
    <input id="description" name="description" value="<c:out value="${task.getDescription()}"></c:out>"><br>
    <label for="task_tags">Tags:</label><br>
    <select id="task_tags" multiple name="taskTags" size="4">
        <c:forEach items="${sessionScope.tags}" var="tag">
            <option id="${tag.getId()}" value="${tag.getId()}">${tag.getTitle()}</option>
        </c:forEach>
        <c:forEach items="${sessionScope.userTags}" var="tag">
            <option id="${tag.getId()}" value="${tag.getId()}">${tag.getTitle()}</option>
        </c:forEach>
    </select><br>
    <label for="priority">Priority:</label><br>
    <select id="priority" name="priority">
        <c:forEach items="${priorities}" var="priority">
            <option id="${priority.getValue()}" value="${priority.getValue()}">${priority.getValue()}</option>
        </c:forEach>
    </select><br>
    <label for="status">Status:</label><br>
    <select id="status" name="status">
        <c:forEach items="${statuses}" var="status">
            <option id="${status.getValue()}" value="${status.getValue()}">${status.getValue()}</option>
        </c:forEach>
    </select><br>
    <button type="submit" name="saveUpdate" value="${task.getId()}">Update</button>
</form>
</body>
<script>
    function setSelectedAttributes() {
        document.getElementById('${task.getPriority()}').selected = true
        document.getElementById('${task.getStatus()}').selected = true

        let tagsIds = '${taskTags}'
        let tags = tagsIds.split(' ');
        tags.forEach(tag => document.getElementById(tag).selected = true)
    }
</script>
</html>