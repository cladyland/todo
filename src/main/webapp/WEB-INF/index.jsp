<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="basis/head.jsp"/>
</head>
<body onload="checkLoginRespStatus(${pageContext.response.status})">
<div class="grid-top">
    <h2 class="welcome-title">Welcome to the web application TODO</h2>
    <h4 class="welcome-more">Log in to continue or register to start using your personal todo-notebook.</h4>
</div>
<div class="grid-bottom">
    <form action="${pageContext.request.contextPath}/" method="post">
        <div class="text-center">
            <input name="username" placeholder="login">
            <br><br>
            <input type="password" name="password" placeholder="password">
            <p></p>
            <p><span id="error" class="badge rounded-pill text-bg-warning"></span></p>
            <button type="submit" class="btn btn-outline-primary">log in</button>
            <a href="${pageContext.request.contextPath}/register">
                <button type="button" class="btn btn-outline-secondary">register</button>
            </a>
        </div>
    </form>
</div>
</body>
</html>
