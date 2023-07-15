<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<jsp:include page="../basis/head.jsp"/>
<body onload="checkRegisterRespStatus(${pageContext.response.status})">
<div class="grid-top">
    <h3 class="register-title">Hello, guest! To register, fill out the form below.</h3>
</div>
<div class="grid-bottom">
    <form action="${pageContext.request.contextPath}/register" method="post">
        <label for="username">username:</label><br>
        <div class="text-center">
            <input id="username" name="username" minlength="3" maxlength="20" required size="30"
                   value="${pageContext.request.getAttribute("username")}">
            <p><span id="wrong_username" class="badge text-bg-warning"></span></p>
        </div>
        <label for="first_name">first name:</label><br>
        <div class="text-center">
            <input id="first_name" name="firstName" minlength="3" maxlength="50" required size="30"
                   value="${pageContext.request.getAttribute("firstName")}">
            <p><span id="wrong_first_name" class="badge text-bg-warning"></span></p>
        </div>
        <label for="last_name">last name:</label><br>
        <div class="text-center">
            <input id="last_name" name="lastName" minlength="3" maxlength="50" required size="30"
                   value="${pageContext.request.getAttribute("lastName")}">
            <p><span id="wrong_last_name" class="badge text-bg-warning"></span></p>
        </div>
        <label for="password">password:</label><br>
        <div class="text-center">
            <input id="password" type="password" name="password" minlength="8" maxlength="32" required size="30">
            <p><span id="wrong_password" class="badge text-bg-warning"></span></p>
        </div>
        <div class="text-center">
            <button class="btn btn-success" type="submit">register</button>
            <p></p>
            <a href="${pageContext.request.contextPath}/">
                <button type="button" class="btn btn-outline-info" title="return to login page">back</button>
            </a>
        </div>
    </form>
</div>
<input hidden="hidden" id="errors" value='${pageContext.request.getAttribute("errors")}'>
<input hidden="hidden" id="error" value='${pageContext.request.getAttribute("error")}'>
</body>
</html>
