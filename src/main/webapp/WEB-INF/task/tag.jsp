<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<jsp:include page="../basis/head.jsp"/>
<jsp:include page="../basis/logout.jsp"/>
<body>
<h3 class="text-shadow">Here your tags:</h3>
<div class="tag-table">
    <c:forEach items="${sessionScope.tags}" var="tag">
        <span style="background-color: ${tag.getColor()}"><i class="invisible">_</i>${tag.getTitle()}<i
                class="invisible">_</i></span>&nbsp;
    </c:forEach>
    <c:forEach items="${sessionScope.userTags}" var="tag">
        <span style="background-color: ${tag.getColor()}"><i class="invisible">_</i>${tag.getTitle()}<i
                class="invisible">_</i></span>&nbsp;
    </c:forEach>
</div>
<div class="grid-bottom">
    <div class="text-center">
        <h5 class="text-shadow"><b>To create a new tag fill the form below:</b></h5>
        <div class="card border-success mb-3" style="max-width: 30rem;">
            <form action="${pageContext.request.contextPath}/todo/tags" method="post">
                <div class="card-body text-success">
                    <div class="card-text">
                        <p>
                            <label for="title">Tag name:</label>
                            <input id="title" name="title" maxlength="15">
                        </p>
                        <p><span style="color: red">${pageContext.request.getAttribute("error")}</span></p>
                        <p>
                            <label for="color">Tag color:</label>
                            <input id="color" type="color" name="color" value="#00ffe1">
                        </p>
                    </div>
                </div>
                <div class="card-footer bg-transparent border-success">
                    <button type="submit" class="btn btn-outline-success">add tag</button>
                    <a href="${pageContext.request.contextPath}${sessionScope.get("returnLinkFromTag")}">
                        <button type="button" class="btn btn-outline-info"
                                title="${sessionScope.get("returnTitleFromTag")}">back
                        </button>
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
