<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body onload="checkRespStatus(${pageContext.response.status})">
${task.getTitle()}<br>
${task.getDescription()}<br>
${task.getPriority()}<br>
${task.getStatus()}<br>
<c:forEach items="${task.getTags()}" var="tag">
    ${tag.getTitle()}
</c:forEach>
<br>
<form action="${pageContext.request.contextPath}/todo/comment" method="post">
    <label for="comment">Left comment:</label><br>
    <input id="comment" name="comment"><br>
    <span id="wrong_comment"></span><br>
    <button type="submit" name="taskId" value="${task.getId()}">add comment</button>
</form><br>
COMMENTS:
<br>
<c:forEach items="${task.getComments()}" var="comment">
    ${comment.getUsername()}<br>
    ${comment.getContents()}<br>
    ${comment.getCreateDate()}<br><br>
</c:forEach>
</body>
<script>
   function checkRespStatus(status){
       if (status === 400){
           let wrong_comment = document.getElementById("wrong_comment")
           wrong_comment.textContent = "Please, write something"
           wrong_comment.style.color = "red"
       }
   }
</script>
</html>
