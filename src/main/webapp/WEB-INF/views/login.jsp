<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html>
<head>
    <jsp:include page="fragments/styles-and-scripts.jsp"/>
    <title>Test project</title>
</head>
<body>
<div class="jumbotron vertical-center">
    <div class="login-div">
        <legend>
            Test project
        </legend>

        <ul class="nav nav-tabs no-bottom-margin">
            <li class="active"><a href="#loginTab" id="loginTabLink" data-toggle="tab" aria-expanded="true">Log in</a></li>
            <li class=""><a href="#registerTab" id="registerTabLink" data-toggle="tab" aria-expanded="false">Register</a></li>
        </ul>

        <div id="myTabContent" class="tab-content">

            <%-- Log in tab --%>
            <div id="loginTab" class="tab-pane active in">
                <div class="panel panel-default">
                    <div class="panel-body">

                        <c:if test="${message != null}">
                            <div class="alert alert-dismissible alert-danger">
                                <button type="button" class="close" data-dismiss="alert">&times;</button>
                                    ${message}
                            </div>
                        </c:if>

                        <%-- Login form --%>
                        <spring:url value="/login" var="loginUrl"/>
                        <form id="loginForm" action="${loginUrl}" method="POST" class="form-horizontal">
                            <fieldset>

                                <%-- Name --%>
                                <c:set var="field" value="name"/>
                                <div class="form-group">
                                    <label for="${field}" class="col-lg-3 control-label">Name</label>
                                    <div class="col-lg-8">
                                        <input type="text" name="${field}" value="<c:out value="${name}"/>"
                                               id="${field}LoginField" class="form-control" minlength="3" maxlength="20"
                                               autofocus onfocus="this.value = this.value;"/>
                                    </div>
                                </div>

                                <%-- Password --%>
                                <c:set var="field" value="password"/>
                                <div class="form-group">
                                    <label for="${field}" class="col-lg-3 control-label">Password</label>
                                    <div class="col-lg-8">
                                        <input type="password" name="${field}" id="${field}LoginField" class="form-control"
                                               minlength="8" maxlength="20"/>
                                    </div>
                                </div>

                                <sec:csrfInput/>

                                <%-- Login button --%>
                                <div class="form-group">
                                    <div class="text-center">
                                        <button type="submit" id="loginButton" class="btn btn-primary login-btn">Log in</button>
                                    </div>
                                </div>

                            </fieldset>
                        </form>

                    </div>
                </div>
            </div>

            <%-- Register tab --%>
            <div id="registerTab" class="tab-pane">
                <div class="panel panel-default">
                    <div class="panel-body">

                        <%-- Register form --%>
                        <spring:url value="/create_user" var="formUrl"/>
                        <form:form action="${formUrl}" method="POST" modelAttribute="newUser" id="registerForm"
                                   cssClass="form-horizontal">
                            <fieldset>
                                <c:set var="widthClass" value="col-lg-8"/>

                                    <%-- Name --%>
                                <c:set var="field" value="name"/>
                                <spring:message var="restrictiona" code="user.name.restrictions"/>
                                <spring:bind path="${field}">
                                    <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                                        <label for="${field}" class="col-lg-3 control-label">Name</label>
                                        <div class="${widthClass}">
                                            <form:input path="${field}" id="${field}RegisterField" cssClass="form-control"
                                                        minlength="3" maxlength="20" data-toggle="tooltip" data-placement="right"
                                                        title="${restrictiona}" data-html="true"/>
                                            <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                            </c:if>

                                        </div>
                                    </div>
                                </spring:bind>

                                    <%-- Email --%>
                                <c:set var="field" value="email"/>
                                <spring:message var="restrictiona" code="user.email.restrictions"/>
                                <spring:bind path="${field}">
                                    <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                                        <label for="${field}" class="col-lg-3 control-label">Email</label>
                                        <div class="${widthClass}">
                                            <form:input path="${field}" id="${field}RegisterField" cssClass="form-control"
                                                        minlength="5" maxlength="50" data-toggle="tooltip" data-placement="right"
                                                        title="${restrictiona}" data-html="true"/>
                                            <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                            </c:if>
                                        </div>
                                    </div>
                                </spring:bind>

                                    <%-- Password --%>
                                <c:set var="field" value="password"/>
                                <spring:message var="restrictiona" code="user.password.restrictions"/>
                                <spring:bind path="${field}">
                                    <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                                        <label for="${field}" class="col-lg-3 control-label">Password</label>
                                        <div class="${widthClass}">
                                            <form:password path="${field}" id="${field}RegisterField" cssClass="form-control"
                                                           minlength="1" maxlength="50" data-toggle="tooltip" data-placement="right"
                                                           title="${restrictiona}" data-html="true"/>
                                            <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                            </c:if>
                                        </div>
                                    </div>
                                </spring:bind>

                                    <%-- Password confirm--%>
                                <c:set var="field" value="passwordConfirm"/>
                                <spring:bind path="${field}">
                                    <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                                        <label for="${field}" class="col-lg-3 control-label">Confirm pass.</label>
                                        <div class="${widthClass}">
                                            <form:password path="${field}" id="${field}RegisterField" cssClass="form-control"
                                                           minlength="5" maxlength="50"/>
                                            <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                            </c:if>
                                        </div>
                                    </div>
                                </spring:bind>

                                <input type="hidden" name="register" value="true"/>

                                    <%-- Register button--%>
                                <div class="form-group">
                                    <div class="text-center">
                                        <button type="submit" class="btn btn-primary login-btn" id="registerButton"
                                                title="Register">Register
                                        </button>
                                    </div>
                                </div>

                            </fieldset>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>


    </div>
</div>

<%-- Tabs control script --%>
<script type="text/javascript">
    function activateTab(tab){
        $('.nav-tabs a[href="#' + tab + '"]').tab('show');
    }

    <c:if test='${not empty registerFail}'>
        activateTab('registerTab');
    </c:if>

    $(document).ready(function () {

        $("#registerTabLink").click(function () {
            setTimeout(function () {
                $("#nameRegisterField").focus();
            }, 20)
        });

        $("#loginTabLink").click(function () {
            setTimeout(function () {
                $("#nameLoginField").focus();
            }, 20)
        })
    })
</script>

</body>
</html> 