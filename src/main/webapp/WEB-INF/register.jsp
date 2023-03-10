<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<p>
    Hello, guest! To register, fill out the form below.
</p>
<form action="${pageContext.request.contextPath}/register" method="post">
    <label for="username">Username:</label><br>
    <input id="username" name="username" minlength="3" maxlength="20" required size="30"><br><br>
    <label for="firstName">First Name:</label><br>
    <input id="firstName" name="firstName" minlength="3" maxlength="20" required size="30"><br><br>
    <label for="lastName">Last Name:</label><br>
    <input id="lastName" name="lastName" minlength="3" maxlength="20" required size="30"><br><br>
    <label for="password">Password:</label><br>
    <input id="password" name="password" minlength="3" maxlength="20" required size="30"><br><br>
    <button class="btn btn-outline-success" type="submit">name myself</button>
</form>
</body>

</html>
