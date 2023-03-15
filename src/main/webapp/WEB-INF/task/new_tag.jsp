<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../basis/head.jsp"/>
</head>
<body>
<div class="position-absolute top-50 start-50 translate-middle">
<form action="${pageContext.request.contextPath}/todo/new-tag" method="post">
    <input name="title">
    <br><br>
    <input type="color" name="color" value="#0000ff">
    <button type="submit">Add tag</button>
</form>
</div>
</body>
</html>
