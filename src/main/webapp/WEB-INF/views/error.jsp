<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <jsp:include page="fragments/styles-and-scripts.jsp"/>
    <title>Test project</title>
</head>
<body>

<div class="container">

    <%-- Message --%>
    <c:if test="${not empty message}">
        <div class="alert alert-danger">
                ${message}
        </div>
    </c:if>
        Go to the <a href='<spring:url value="/"/>'>main page</a>

</div>
</body>
</html>