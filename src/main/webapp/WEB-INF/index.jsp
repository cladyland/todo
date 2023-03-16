<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <jsp:include page="basis/head.jsp"/>
</head>
<body onload="checkResponseStatus(${pageContext.response.status})">
<div class="position-absolute top-50 start-50 translate-middle">
    <form action="${pageContext.request.contextPath}/" method="post">
        <input name="username" placeholder="login"><br><br>
        <input type="password" name="password" placeholder="password">
        <p>
        <center><span id="error" class="badge rounded-pill text-bg-warning"></span></center>
        </p>
        <button type="submit" class="btn btn-outline-primary">login</button>
    </form>
    <form action="${pageContext.request.contextPath}/register" method="get">
        <button type="submit" class="btn btn-outline-secondary">register</button>
    </form>
</div>
</body>
<script>
    function checkResponseStatus(status) {
        if (status === 400) {
            setMessageParam("Please, enter your login and password")
        } else if (status === 401) {
            setMessageParam("Wrong login or password")
        }
    }

    function setMessageParam(content) {
        let error_message = document.getElementById("error")
        error_message.textContent = content
    }
</script>
</html>
