<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<jsp:include page="../basis/head.jsp"/>
<jsp:include page="../basis/logout.jsp"/>
<script src="${pageContext.request.contextPath}/js/new_task.js" type="module"></script>
<body>
<div class="grid-bottom" style="padding-top: 10px">
    <form action="${pageContext.request.contextPath}/todo/new-task" method="post">
        <p>
            <label for="title">Title:</label><br>
            <input id="title" name="title" maxlength="50">
        </p>
        <p>
            <span class="badge text-bg-warning">${pageContext.request.getAttribute("error")}</span>
        </p>
        <p>
            <label for="description">Description:</label><br>
            <textarea class="task-desc-area" id="description" name="description" placeholder="write more information"
                      maxlength="500" onkeyup="countCharacters(500, this.value)"></textarea>
            <br>
            <i id="count"></i>
        </p>
        <p>
            <label for="task_tags">
                <b title="To select several options, hold down Ctrl (Windows) or Command (Mac)">&#x1F6C8</b>Tags:</label>
            <br>
            <select id="task_tags" multiple name="taskTags" size="4">
                <c:forEach items="${sessionScope.tags}" var="tag">
                    <option id="${tag.getId()}" value="${tag.getId()}"
                            style="background-color: ${tag.getColor()}">${tag.getTitle()}</option>
                </c:forEach>
                <c:forEach items="${sessionScope.userTags}" var="tag">
                    <option id="${tag.getId()}" value="${tag.getId()}"
                            style="background-color: ${tag.getColor()}">${tag.getTitle()}</option>
                </c:forEach>
            </select>
        </p>
        <p>
            <a href="${pageContext.request.contextPath}/todo/tags">
                <button type="button" class="btn btn-success">Add new tag</button>
            </a>
        </p>
        <p>
            <label for="priority">Priority:</label>
            <select id="priority" name="priority">
                <c:forEach items="${priorities}" var="priority">
                    <option id="${priority.getValue()}" value="${priority.getValue()}">${priority.getValue()}</option>
                </c:forEach>
            </select>
        </p>
        <p>
            <label for="status">Status:</label>
            <select id="status" name="status">
                <c:forEach items="${statuses}" var="status">
                    <option id="${status.getValue()}" value="${status.getValue()}">${status.getValue()}</option>
                </c:forEach>
            </select>
        </p>
        <button type="submit" class="btn btn-primary" onclick="cleanTaskStorage()">Add new task</button>
        <a href="${pageContext.request.contextPath}/todo">
            <button type="button" class="btn btn-outline-info" title="return to tasks list">back</button>
        </a>
    </form>
</div>
</body>
</html>
