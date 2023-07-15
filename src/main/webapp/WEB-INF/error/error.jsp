<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/todo-st.css">
    <title>Error</title>
</head>
<body style="background-color: #E6E6FA">
<div class="position-absolute top-50 start-50 translate-middle">
    <div class="error">
        <h1>${pageContext.response.status}</h1>
        <h2>Oops! Something went wrong :(</h2>
        <h3>Contact technical support: <a href="#">admin@todo.com</a></h3>
    </div>
    <div class="text-center">
        <a href="${pageContext.request.contextPath}/">
            <button type="button" class="btn btn-outline-primary">back to homepage</button>
        </a>
    </div>
</div>
</body>
</html>
