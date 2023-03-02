<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/" method="post">
<input name="username">
<input name="password">
<button type="submit">login</button>
</form>
<form action="${pageContext.request.contextPath}/register" method="get">
    <button type="submit">register</button>
</form>
</body>
</html>
