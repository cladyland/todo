<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/register" method="post">
    <input name="username" minlength="3" maxlength="20" required size="30" value="username">
    <input name="firstName" minlength="3" maxlength="20" required size="30" value="firstName">
    <input name="lastName" minlength="3" maxlength="20" required size="30" value="lastName">
    <input name="password" minlength="3" maxlength="20" required size="30" value="password">
    <button class="btn btn-outline-success" type="submit">name myself</button>
</form>
</body>
</html>
