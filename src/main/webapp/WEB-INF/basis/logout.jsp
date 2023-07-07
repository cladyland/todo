<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form class="logout-form" action="${pageContext.request.contextPath}/out" method="get">
    <button type="submit" class="btn btn-dark" onclick="cleanTaskStorage()">log out</button>
</form>
