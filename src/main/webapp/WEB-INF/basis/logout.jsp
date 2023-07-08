<%@ page contentType="text/html;charset=UTF-8" %>
<form class="logout-form" action="${pageContext.request.contextPath}/out" method="get">
    <button type="submit" class="btn btn-dark" onclick="cleanTaskStorage()">log out</button>
</form>
