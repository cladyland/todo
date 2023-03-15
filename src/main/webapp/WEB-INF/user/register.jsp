<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <jsp:include page="../basis/head.jsp"/>
</head>
<body onload="checkResponseStatus(${pageContext.response.status})">
<div class="position-absolute top-50 start-50 translate-middle">
    <center>
        <h2>
            Hello, guest! To register, fill out the form below.
        </h2></center>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <center>
            <input id="username" name="username" placeholder="username" maxlength="20" required size="30"
                   value="${pageContext.request.getAttribute("username")}">
            <p><span id="wrong_username" class="badge text-bg-warning"></span></p>
            <input id="first_name" name="firstName" placeholder="first name" maxlength="20" required size="30"
                   value="${pageContext.request.getAttribute("firstName")}">
            <p><span id="wrong_first_name" class="badge text-bg-warning"></span></p>
            <input id="last_name" name="lastName" placeholder="last name" maxlength="20" required size="30"
                   value="${pageContext.request.getAttribute("lastName")}">
            <p><span id="wrong_last_name" class="badge text-bg-warning"></span></p>
            <input id="password" name="password" placeholder="password" maxlength="20" required size="30"><br><br>
            <button class="btn btn-success" type="submit">register</button>
        </center>
    </form>
</div>
</body>
<script>
    function checkResponseStatus(status) {
        if (status === 400) {
            setErrorMessageToBlankFields()
        } else if (status === 422) {
            let error_message = '${pageContext.request.getAttribute("error")}';
            let message_id;
            if (error_message.startsWith("Username")) {
                message_id = "wrong_username"
            } else if (error_message.startsWith("First")) {
                message_id = "wrong_first_name"
            } else if (error_message.startsWith("Last")) {
                message_id = "wrong_last_name"
            }
            setMessageParamToWrongField(message_id, error_message)
        }
    }

    function setMessageParamToWrongField(message_id, error_message) {
        let wrong = document.getElementById(message_id)
        wrong.textContent = error_message
    }

    function setErrorMessageToBlankFields() {
        let username = document.getElementById("username").value
        let first_name = document.getElementById("first_name").value
        let last_name = document.getElementById("last_name").value
        if (username === "") {
            setMessageParamToBlankFields("wrong_username", "username")
        }
        if (first_name === "") {
            setMessageParamToBlankFields("wrong_first_name", "first name")
        }
        if (last_name === "") {
            setMessageParamToBlankFields("wrong_last_name", "last name")
        }
    }

    function setMessageParamToBlankFields(message_id, blank_field) {
        let message = "Please, enter your "
        let wrong_span = document.getElementById(message_id)
        wrong_span.textContent = message + blank_field
    }
</script>
</html>
