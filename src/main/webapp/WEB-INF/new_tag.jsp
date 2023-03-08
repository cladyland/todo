<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/todo/new-tag" method="post">
    <input name="title">
    <br>
    <input name="color">
    <button type="submit">Add tag</button>
</form>
</body>
</html>
