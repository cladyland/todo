<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<jsp:include page="../basis/head.jsp"/>
<jsp:include page="../basis/logout.jsp"/>
<body onload="setSelectedTaskAttributes('${task.getPriority()}', '${task.getStatus()}', '${taskTags}')">
<div class="grid-bottom" style="padding-top: 10px">
    <form action="${pageContext.request.contextPath}/todo" method="post">
        <p>
            <label for="title">Title:</label><br>
            <input id="title" maxlength="50" name="title" value="<c:out value="${task.getTitle()}"></c:out>">
        </p>
        <p><span class="badge text-bg-warning">${pageContext.request.getAttribute("error")}</span></p>
        <p>
            <label for="description">Description:</label><br>
            <textarea class="task-desc-area" id="description" name="description" maxlength="500"
                      onkeyup="countCharacters(500, this.value)">${task.getDescription()}</textarea>
            <br>
            <i id="count"></i>
        </p>
        <p>
            <label for="task_tags">Tags:</label><br>
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
            <label for="priority">Priority:</label><br>
            <select id="priority" name="priority">
                <c:forEach items="${priorities}" var="priority">
                    <option id="${priority.getValue()}" value="${priority.getValue()}">${priority.getValue()}</option>
                </c:forEach>
            </select>
        </p>
        <p>
            <label for="status">Status:</label><br>
            <select id="status" name="status">
                <c:forEach items="${statuses}" var="status">
                    <option id="${status.getValue()}" value="${status.getValue()}">${status.getValue()}</option>
                </c:forEach>
            </select>
        </p>
        <button type="submit" name="saveUpdate" value="${task.getId()}" class="btn btn-outline-primary">update</button>
        <a href="${pageContext.request.contextPath}/todo">
            <button type="button" class="btn btn-outline-info" title="return to tasks list">back</button>
        </a>
    </form>
</div>
</body>
</html>
