<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <jsp:include page="../basis/head.jsp"/>
    <style>
        div {
            position: relative;
            margin: 0 auto;
            clear: left;
            height: auto;
            z-index: 0;
            text-align: center;
        }
    </style>
</head>
<body onload="checkRespStatus(${pageContext.response.status})">
<div>
    <table class="table table-success table-striped" style="width: 40%">
        <tr>
            <td style="width: 20%">Title:</td>
            <td>${task.getTitle()}</td>
        </tr>
        <tr>
            <td style="width: 20%">Description:</td>
            <td>${task.getDescription()}</td>
        </tr>
        <tr>
            <td style="width: 20%">Priority:</td>
            <td>${task.getPriority()}</td>
        </tr>
        <tr>
            <td style="width: 20%">Status:</td>
            <td>${task.getStatus()}</td>
        </tr>
        <tr>
            <td style="width: 20%">Tags:</td>
            <td><c:forEach items="${task.getTags()}" var="tag">
                <span style="background-color: ${tag.getColor()}">${tag.getTitle()}</span>
            </c:forEach></td>
        </tr>
    </table>
    <br><br>
    <form action="${pageContext.request.contextPath}/todo/comment" method="post">
        <label for="comment">Left comment:</label><br>
        <textarea id="comment" name="comment" style="width: 400px" placeholder="write your comment here"></textarea><br>
        <span id="wrong_comment"></span><br>
        <button type="submit" name="taskId" value="${task.getId()}" class="btn btn-light">add comment</button>
    </form>
    <br>
    <b>COMMENTS:</b>
    <br>
    <c:forEach items="${task.getComments()}" var="comment">
        ${comment.getUsername()}<br>
        ${comment.getContents()}<br>
        ${comment.getCreateDate()}<br><br>
    </c:forEach>
</div>
</body>
<script>
    function checkRespStatus(status) {
        if (status === 400) {
            let wrong_comment = document.getElementById("wrong_comment")
            wrong_comment.textContent = "Please, write something"
            wrong_comment.style.color = "red"
        }
    }
</script>
</html>
