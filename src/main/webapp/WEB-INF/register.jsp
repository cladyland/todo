<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body onload="checkResponseStatus(${pageContext.response.status})">
<p>
    Hello, guest! To register, fill out the form below.
</p>
<form action="${pageContext.request.contextPath}/register" method="post">
    <label for="username">Username:</label><br>
    <input id="username" name="username" minlength="0" maxlength="20" required size="30"
           value="${pageContext.request.getAttribute("username")}"><br>
    <span id="wrong_username"></span><br><br>

    <label for="first_name">First Name:</label><br>
    <input id="first_name" name="firstName" minlength="0" maxlength="20" required size="30"
           value="${pageContext.request.getAttribute("firstName")}"><br>
    <span id="wrong_first_name"></span><br><br>

    <label for="last_name">Last Name:</label><br>
    <input id="last_name" name="lastName" minlength="0" maxlength="20" required size="30"
           value="${pageContext.request.getAttribute("lastName")}"><br>
    <span id="wrong_last_name"></span><br><br>

    <label for="password">Password:</label><br>
    <input id="password" name="password" minlength="0" maxlength="20" required size="30"><br><br>
    <button class="btn btn-outline-success" type="submit">name myself</button>
</form>
</body>

<script>
    function checkResponseStatus(status) {
        if (status === 400) {
            setErrorMessageToBlankFields()
        }
    }

    function setErrorMessageToBlankFields() {
        let username = document.getElementById("username").value
        let first_name = document.getElementById("first_name").value
        let last_name = document.getElementById("last_name").value
        let message = "Please, enter your "

        if (username === "") {
            let wrong = document.getElementById("wrong_username")
            wrong.textContent = message + "username"
            wrong.style.color = "orange"
        }
        if (first_name === "") {
            let wrong = document.getElementById("wrong_first_name")
            wrong.textContent = message + "first name"
            wrong.style.color = "orange"
        }
        if (last_name === "") {
            let wrong = document.getElementById("wrong_last_name")
            wrong.textContent = message + "last name"
            wrong.style.color = "orange"
        }
    }
</script>
</html>
