<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>TODO</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body onload="checkResponseStatus(${pageContext.response.status})">
<form action="${pageContext.request.contextPath}/" method="post">
    <label for="username">Login:</label><br>
    <input id="username" name="username"><br><br>
    <label for="password">Password:</label><br>
    <input id="password" name="password"><br>
    <span id="error"></span><br><br>
    <button type="submit">login</button>
</form>
<form action="${pageContext.request.contextPath}/register" method="get">
    <button type="submit">register</button>
</form>
</body>
<script>
    function checkResponseStatus(status) {
        let error_message = document.getElementById("error")
        if (status === 400) {
            error_message.textContent = "Please, enter your login and password"
            error_message.style.color = "orange"
        } else if (status === 401) {
            error_message.textContent = "Wrong login or password"
            error_message.style.color = "red"
        } else {
            error_message.textContent = ""
        }
    }
</script>
</html>
