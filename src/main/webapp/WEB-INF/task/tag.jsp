<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="../basis/head.jsp"/>
</head>
<body>
<center>
    <br><br>
    <h3>Here your tags:</h3>
    <c:forEach items="${sessionScope.tags}" var="tag">
    <span style="background-color: ${tag.getColor()}">
            ${tag.getTitle()}
    </span>&nbsp;
    </c:forEach>
    <c:forEach items="${sessionScope.userTags}" var="tag">
    <span style="background-color: ${tag.getColor()}">
            ${tag.getTitle()}
    </span>&nbsp;
    </c:forEach>
</center>
<div class="position-absolute top-50 start-50 translate-middle">
    <h4>To create a new tag fill the form below:</h4>
    <form action="${pageContext.request.contextPath}/todo/tags" method="post">
        <p><label for="title">Tag name: </label>
            <input id="title" name="title">
        </p>
        <p><label for="color">Tag color: </label>
            <input id="color" type="color" name="color" value="#00ffe1">
        </p>
        <button type="submit" class="btn btn-outline-success">add tag</button>
    </form>
    <form action="${pageContext.request.contextPath}/todo" method="get">
        <button type="submit" class="btn btn-outline-info" title="return to tasks list">back</button>
    </form>
</div>
</body>
</html>
