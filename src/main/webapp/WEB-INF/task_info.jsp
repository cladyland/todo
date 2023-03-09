<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
${task.getTitle()}<br>
${task.getDescription()}<br>
${task.getPriority()}<br>
${task.getStatus()}<br>
<c:forEach items="${task.getTags()}" var="tag">
    ${tag.getTitle()}
</c:forEach>
COMMENTS:
<br>
<c:forEach items="${task.getComments()}" var="comment">
    ${comment.getUsername()}<br>
    ${comment.getContents()}<br>
    ${comment.getCreateDate()}<br><br>
</c:forEach>
<form action="${pageContext.request.contextPath}/todo/comment" method="post">
    <input name="comment">
    <button type="submit" name="task" value="${task.getId()}">add comment</button>
</form>
</body>
</html>
