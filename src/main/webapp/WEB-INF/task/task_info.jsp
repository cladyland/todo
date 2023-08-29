<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<jsp:include page="../basis/head.jsp"/>
<jsp:include page="../basis/logout.jsp"/>
<body>
<div class="task-info">
    <table class="table table-success table-striped" style="width: 40%;font-size: large;">
        <tr>
            <td class="task-info-td"><b>Title:</b></td>
            <td><i>${task.getTitle()}</i></td>
        </tr>
        <tr>
            <td class="task-info-td"><b>Description:</b></td>
            <td>${task.getDescription()}</td>
        </tr>
        <tr>
            <td class="task-info-td"><b>Priority:</b></td>
            <td><i>${task.getPriority()}</i></td>
        </tr>
        <tr>
            <td class="task-info-td"><b>Status:</b></td>
            <td><i>${task.getStatus()}</i></td>
        </tr>
        <tr>
            <td class="task-info-td"><b>Tags:</b></td>
            <td>
                <c:forEach items="${task.getTags()}" var="tag">
                    <span style="background-color: ${tag.getColor()}">${tag.getTitle()}</span>
                </c:forEach>
            </td>
        </tr>
    </table>
    <br><br>
    <form action="${pageContext.request.contextPath}/todo/comment" method="post">
        <textarea name="comment" style="width: 500px; height: 100px" maxlength="300"
                  placeholder="write your comment here" onkeyup="countCharacters(300, this.value)"></textarea>
        <p>
            <i id="count"></i>
            <br>
            <span id="wrong_comment"></span>
        </p>
        <button type="submit" name="taskId" value="${task.getId()}" class="btn btn-outline-success">add comment</button>
        <a href="${pageContext.request.contextPath}/todo">
            <button type="button" class="btn btn-outline-info" title="return to tasks list">back</button>
        </a>
    </form>
    <p class="text-shadow"><b>COMMENTS:</b></p>
</div>
<div class="comment">
    <c:forEach items="${task.getComments()}" var="comment">
        <div class="card" style="max-width: 40rem;">
            <div class="card-header">${comment.getUsername()}</div>
            <div class="card-body">
                <blockquote class="blockquote mb-0">
                    <p>${comment.getContents()}</p>
                    <footer class="blockquote-footer">${comment.getCreateDate()}</footer>
                </blockquote>
            </div>
        </div>
        <br>
    </c:forEach>
</div>
<input hidden="hidden" id="status" value='${pageContext.response.status}'>
<script>
    window.onload = function () {
        let status = document.getElementById('status').value

        if (status === '400') {
            let wrong_comment = document.getElementById('wrong_comment')
            wrong_comment.textContent = 'Please, write something'
            wrong_comment.style.color = 'red'
        }
    }
</script>
</body>
</html>
